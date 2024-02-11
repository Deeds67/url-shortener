package pierremarais.urlshortener

interface ShortenedURLRepository {
    fun save(shortenedURL: ShortenedURL)
    fun findByShortenedURL(shortURL: String): ShortenedURL?
    fun findByOriginalURL(originalURL: String): ShortenedURL?
}
