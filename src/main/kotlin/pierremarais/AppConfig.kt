package pierremarais

import com.sksamuel.hoplite.ConfigLoader

class AppConfig() {
    companion object {
        fun fromConfigFile(): AppConfig {
            return ConfigLoader().loadConfigOrThrow("/application.conf")
        }
    }
}