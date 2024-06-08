package spring.test.configuration

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainerProvider

class PostgresTestContainersInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    companion object {
        val POSTGRES_CONTAINER: JdbcDatabaseContainer<out JdbcDatabaseContainer<*>> =
            PostgreSQLContainerProvider().newInstance("14.5-alpine")

        init {
            POSTGRES_CONTAINER.start()
        }
    }

    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
        TestPropertyValues
            .of("spring.datasource.url=" + POSTGRES_CONTAINER.jdbcUrl)
            .and("spring.datasource.username=" + POSTGRES_CONTAINER.username)
            .and("spring.datasource.password=" + POSTGRES_CONTAINER.password)
            .applyTo(configurableApplicationContext.environment)
    }
}
