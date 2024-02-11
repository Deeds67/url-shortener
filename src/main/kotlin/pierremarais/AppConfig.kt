package pierremarais

import com.sksamuel.hoplite.ConfigLoader

data class AppConfig(val database: DatabaseConfig) {
    companion object {
        fun fromConfigFile(): AppConfig {
            return ConfigLoader().loadConfigOrThrow("/application.conf")
        }
    }
}

data class DatabaseConfig(
    val user: String,
    val password: String,
    val host: String,
    val port: String,
    val database: String
)