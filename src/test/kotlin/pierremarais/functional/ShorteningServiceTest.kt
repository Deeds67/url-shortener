package pierremarais.functional

import org.junit.Test
import pierremarais.urlshortener.Base58Strategy
import pierremarais.urlshortener.InMemoryShortenedURLRepository
import pierremarais.urlshortener.ShorteningServiceImpl
import kotlin.test.assertEquals

class ShorteningServiceTest {
    private val strategy = Base58Strategy()

    @Test
    fun `shortening a new url should return a shortened url of length lessThanOrEqualTo 8`() {
        // Given
        val repository = InMemoryShortenedURLRepository()
        val shorteningService = ShorteningServiceImpl(strategy, repository)

        val url = "https://www.google.com"
        // When
        val result = shorteningService.shorten(url)
        val shortURL = result.shortURL
        val created = result.created
        // Then
        assert(shortURL.length <= 8)
        assertEquals(true, created)
    }
}