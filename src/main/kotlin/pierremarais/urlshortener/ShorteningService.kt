package pierremarais.urlshortener

data class ShortenResult(val shortURL: String, val created: Boolean)
interface ShorteningService {
    fun shorten(url: String): ShortenResult
    fun getOriginalURL(shortURL: String): String?
}

class ShorteningServiceImpl(private val shorteningStrategy: ShorteningStrategy,
                            private val repository: ShortenedURLRepository,
                            private val maxRetries: Int = 5): ShorteningService {
    override fun shorten(url: String): ShortenResult {
        repository.findByOriginalURL(url)?.let { return ShortenResult(it.shortURL, created = false) }
        return ShortenResult(shorteningStrategy.shorten(url), created = true)
    }

    override fun getOriginalURL(shortURL: String): String? {
        throw Exception("Not implemented")
    }
}