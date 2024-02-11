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

        var shortenedURL = shorteningStrategy.shorten(url)

        repeat(maxRetries) { retry ->
            val uniqueIdentifier = System.currentTimeMillis().toString() + retry

            if (repository.findByShortenedURL(shortenedURL) == null) {
                repository.save(ShortenedURL(url, shortenedURL))
                return@shorten ShortenResult(shortenedURL, created = true)
            }
            shortenedURL = shorteningStrategy.shorten(url + uniqueIdentifier)
        }

        throw Exception("Failed to generate a unique shortened URL after $maxRetries retries.")
    }

    override fun getOriginalURL(shortURL: String): String? =
        repository.findByShortenedURL(shortURL)?.let { return it.originalURL }
}