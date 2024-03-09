
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val exposed_version: String by project
val hikari_version: String by project
val flyway_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("org.flywaydb.flyway") version "8.5.4"
}

group = "${rootProject.name}.data"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")

    testImplementation(kotlin("test"))
}

flyway {
    url = "jdbc:postgresql://localhost:6432/testdb"
    user = "testpguser"
    password = "testpgpass"
    baselineOnMigrate = true
    validateMigrationNaming = true
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}