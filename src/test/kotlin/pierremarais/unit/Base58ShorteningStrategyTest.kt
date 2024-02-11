package pierremarais.unit

import junit.framework.TestCase.assertEquals
import org.junit.Test
import pierremarais.urlshortener.Base58Strategy
import kotlin.test.assertNotEquals

class Base58ShorteningStrategyTest {
    private val base58Shortening = Base58Strategy()

    @Test
    fun `shortening a url should return a shortened url that is not longer than 8 characters`() {
        // Given
        val url = "https://www.google.com/"
        // When
        val result = base58Shortening.shorten(url)
        // Then
        assert(result.length <= 8)
    }

    @Test
    fun `shortening a url should return a base58 encoded string`() {
        val base58Chars = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"

        // Given
        val url = "https://www.google.com/"
        // When
        val result = base58Shortening.shorten(url)
        // Then
        result.forEach {
            assert(base58Chars.contains(it))
        }
    }

    @Test
    fun `shorten should return a different shortened string for different urls`() {
        // Given
        val url1 = "https://www.google.com/234982394087293847293472938472938472938472389472938472938472983423423423"
        val url2 =
            "https://www.google.com/aaaaaaaaaaaaasssssssssssssssddddddddddddddddddddffffffffffffffffffffffffffffffffffff"
        // When
        val result = base58Shortening.shorten(url1)
        val result2 = base58Shortening.shorten(url2)
        // Then
        assert(result.length <= 8)
        assert(result2.length <= 8)

        assertNotEquals(result, result2)
    }

    @Test
    fun `shortening the same url twice should return the same shortened value`() {
        // Given
        val url = "https://www.google.com/234982394087293847293472938472938472938472389472938472938472983423423423"
        // When
        val result = base58Shortening.shorten(url)
        val result2 = base58Shortening.shorten(url)
        // Then
        assert(result.length <= 8)
        assert(result2.length <= 8)

        assertEquals(result, result2)
    }
}