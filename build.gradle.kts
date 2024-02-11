val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val hoplite_version: String by project
val exposed_version: String by project
val postgresql_version: String by project
val mockk_version: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("org.flywaydb.flyway") version "8.5.12"
}

group = "pierremarais"
version = "0.0.1"

application {
    mainClass.set("pierremarais.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

fun getFromEnv(value: String): String? = emptyStringToNull(System.getenv(value))
fun emptyStringToNull(value: String?): String? = if (value?.trim()?.isEmpty() != false) null else value

flyway {
    val dbURL = getFromEnv("DATABASE_HOST") ?: "localhost"
    val dbUser = getFromEnv("DATABASE_USER") ?: "postgres"
    val dbPassword = getFromEnv("DATABASE_PASSWORD") ?: "postgres"
    val dbName = getFromEnv("DATABASE_NAME") ?: "postgres"
    val dbPort = getFromEnv("DATABASE_PORT") ?: "5432"

    url = "jdbc:postgresql://$dbURL:$dbPort/$dbName"
    user = dbUser
    password = dbPassword
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-metrics-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-jackson")

    implementation("com.sksamuel.hoplite:hoplite-core:$hoplite_version")
    implementation("com.sksamuel.hoplite:hoplite-hocon:$hoplite_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    runtimeOnly("org.postgresql:postgresql:$postgresql_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-client-content-negotiation")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:${mockk_version}")
}
