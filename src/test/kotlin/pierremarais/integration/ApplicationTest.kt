package pierremarais.integration

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.server.testing.client.*
import org.junit.Test
import pierremarais.AppConfig
import pierremarais.urlshortener.PostgresShortenedURLRepository
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ApplicationTest {
    @BeforeTest
    fun setup() {
        val config = AppConfig.fromConfigFile()
        val repo = PostgresShortenedURLRepository(config.database)
        repo.clear()
    }

    @Test
    fun `PUT shortened-urls returns a short_url response 201 Created and 200 OK when repeated`() = testApplication {
        // Given
        val url = "https://example.com"

        // When
        val response = client.put("/shortened-urls") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"url\": \"$url\"}")
        }
        val response2 = client.put("/shortened-urls") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"url\": \"$url\"}")
        }
        val shortURL = response.body<String>()
        val shortURL2 = response2.body<String>()

        // Then
        val expectedResult = "{\"short_url\":\"http://localhost:8080/qTi8htAn\"}"
        assertEquals(expectedResult, shortURL)
        assertEquals(shortURL, shortURL2)
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(HttpStatusCode.OK, response2.status)
    }

    @Test
    fun `GET a shortened url redirects to the original url`() = testApplication {
        // Given
        val url = "http://google.com/"

        // When
        val shortenResponse = client.put("shortened-urls") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"url\": \"$url\"}")
        }

        val shortURL = shortenResponse.body<String>()

        // Then
        val expectedResult = "{\"short_url\":\"http://localhost:8080/eb7fbVee\"}"
        assertEquals(expectedResult, shortURL)

        // Ktor's testing client throws an error upon a 302 redirect, so this hack verifies that the redirect is working
        val redirectException = assertFailsWith<InvalidTestRequestException> {
            client.get("eb7fbVee")
        }
        assertEquals(
            "Can not resolve request to http://google.com. Main app runs at localhost:80, localhost:443 and external services are ",
            redirectException.message
        )
    }

    @Test
    fun `GET a non existing short url returns 404`() = testApplication {
        // Given
        val nonExistingShortURL = "nonexist"
        // When
        val response = client.get(nonExistingShortURL)

        // Then
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}