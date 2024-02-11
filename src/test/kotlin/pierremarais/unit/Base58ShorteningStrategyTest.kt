package pierremarais.unit

import org.junit.Test
import pierremarais.urlshortener.Base58Strategy

class Base58ShorteningStrategyTest {
    private val base58Shortening = Base58Strategy()

    @Test
    fun `shortening a url should return a base58 encoded shortened url that is not longer than 8 characters`() {
        // Given
        val url = "https://www.google.com/"
        // When
        val result = base58Shortening.shorten(url)
        // Then
        assert(result.length <= 8)
    }
}