package pierremarais

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import pierremarais.plugins.*
import pierremarais.urlshortener.Base58Strategy
import pierremarais.urlshortener.PostgresShortenedURLRepository
import pierremarais.urlshortener.ShorteningServiceImpl

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val config = AppConfig.fromConfigFile()
    val shortenedURLRepository = PostgresShortenedURLRepository(config.database)
    val shorteningStrategy = Base58Strategy()
    val shorteningService = ShorteningServiceImpl(shorteningStrategy, shortenedURLRepository)

    configureMonitoring()
    configureHTTP()
    configureRouting(shorteningService, baseURL = config.routing.base)
}
