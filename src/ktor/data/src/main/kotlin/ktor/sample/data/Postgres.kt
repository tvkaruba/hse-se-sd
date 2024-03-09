package ktor.sample.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

fun Application.connectToPostgres(): DataSource {
    Class.forName("org.postgresql.Driver")
    environment.config.validatePostgresConfig()
    val (url, user, password) = environment.config.retrievePostgresConfig()
    val dataSource = hikari(url, user, password)

    val f = Flyway.configure().dataSource(dataSource).load()
    f.migrate()

    Database.connect(dataSource)

    transaction {
        val creators = SchemaUtils.statementsRequiredToActualizeScheme()
        if (creators.isNotEmpty()) {
            val output = creators.fold("\n") { acc, c ->
                "$acc\n$c"
            }
            throw Exception(output)
        }
    }

    return dataSource
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

object PostgresConfigs {
    const val PostgresUrl = "postgres.url"
    const val PostgresUser = "postgres.user"
    const val PostgresPassword = "postgres.password"

    fun allRequiredConfigs() : List<String> = listOf(
        PostgresUrl,
        PostgresUser,
        PostgresPassword
    )
}

class PostgresConfigurationException(missingProperty: String) : Exception(missingProperty.trimIndent())

private fun ApplicationConfig.validatePostgresConfig() {
    PostgresConfigs.allRequiredConfigs().forEach {
        propertyOrNull(it) ?: throw PostgresConfigurationException(it)
    }
}

private fun ApplicationConfig.retrievePostgresConfig() : List<String> {
    return listOf(
        property(PostgresConfigs.PostgresUrl).getString(),
        property(PostgresConfigs.PostgresUser).getString(),
        property(PostgresConfigs.PostgresPassword).getString()
    )
}

fun hikari(url: String, user: String, password: String) : DataSource {
    return HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = url
        username = user
        this.password = password
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }.let(::HikariDataSource)
}
