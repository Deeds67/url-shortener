package pierremarais.urlshortener

import java.util.concurrent.ConcurrentHashMap

interface ShortenedURLRepository {
    fun save(shortenedURL: ShortenedURL)
    fun findByShortenedURL(shortURL: String): ShortenedURL?
    fun findByOriginalURL(originalURL: String): ShortenedURL?
}

class InMemoryShortenedURLRepository : ShortenedURLRepository {
    private val shortenedURLToOriginalURL = ConcurrentHashMap<String, String>()
    private val originalURLToShortenedURL = ConcurrentHashMap<String, String>()

    override fun save(shortenedURL: ShortenedURL) {
        shortenedURLToOriginalURL[shortenedURL.shortURL] = shortenedURL.originalURL
        originalURLToShortenedURL[shortenedURL.originalURL] = shortenedURL.shortURL
    }

    override fun findByShortenedURL(shortURL: String): ShortenedURL? {
        val originalURL = shortenedURLToOriginalURL[shortURL]
        return originalURL?.let { ShortenedURL(it, shortURL) }
    }

    override fun findByOriginalURL(originalURL: String): ShortenedURL? {
        val shortenedURL = originalURLToShortenedURL[originalURL]
        return shortenedURL?.let { ShortenedURL(originalURL, it) }
    }
}