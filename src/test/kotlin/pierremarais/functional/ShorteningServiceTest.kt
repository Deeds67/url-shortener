package pierremarais.functional

import org.junit.Test
import pierremarais.urlshortener.Base58Strategy
import pierremarais.urlshortener.InMemoryShortenedURLRepository
import pierremarais.urlshortener.ShorteningServiceImpl
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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

    @Test
    fun `shortening similar urls should return different short urls`() {
        // Given
        val repository = InMemoryShortenedURLRepository()
        val shorteningService = ShorteningServiceImpl(strategy, repository)

        val url1 = "https://www.google.com/1"
        val url2 = "https://www.google.com/2"
        // When
        val result1 = shorteningService.shorten(url1)
        val result2 = shorteningService.shorten(url2)

        val shortURL1 = result1.shortURL
        val shortURL2 = result2.shortURL
        // Then
        assertNotEquals(shortURL1, shortURL2)
        assertTrue { result1.created }
        assertTrue { result2.created }
    }

    @Test
    fun `shortening the same url should return the same short url`() {
        // Given
        val repository = InMemoryShortenedURLRepository()
        val shorteningService = ShorteningServiceImpl(strategy, repository)

        val url1 = "https://www.google.com/1"
        val url2 = "https://www.google.com/1"
        // When
        val result1 = shorteningService.shorten(url1)
        val result2 = shorteningService.shorten(url2)

        val shortURL1 = result1.shortURL
        val shortURL2 = result2.shortURL
        // Then
        assertEquals(shortURL1, shortURL2)
        assertTrue { result1.created }
        assertFalse { result2.created }
    }
}