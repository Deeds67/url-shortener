package pierremarais.plugins

import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pierremarais.urlshortener.ShorteningService

fun Application.configureRouting(shorteningService: ShorteningService, linkPrefix: String = "http://localhost:8080/") {
    install(ContentNegotiation) {
        jackson()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        put("/shortened-urls"){
            data class ShorteningRequest(@JsonProperty("url") val url: String)
            data class ShorteningResponse(@JsonProperty("short_url") val shortUrl: String)
            val body = call.receive<ShorteningRequest>()
            val result = shorteningService.shorten(body.url)
            val shortURLWithPrefix = linkPrefix + result.shortURL

            val statusCode = if (result.created) HttpStatusCode.Created else HttpStatusCode.OK
            call.response.status(statusCode)

            call.respond(ShorteningResponse(shortURLWithPrefix))
        }

        get("/{short-url}"){
            val shortURL = call.parameters["short-url"] ?: throw IllegalArgumentException("Missing short-url parameter")
            println("inside short url route")
            val originalURL = shorteningService.getOriginalURL(shortURL)
            println("original: $originalURL")
            if (originalURL != null) {
                println("responding with originalURL $originalURL")
                call.respondRedirect(originalURL)
            } else {
                call.respondText("404: Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
