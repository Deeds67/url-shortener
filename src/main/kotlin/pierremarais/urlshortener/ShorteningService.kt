package pierremarais.urlshortener

data class ShortenResult(val shortURL: String, val created: Boolean)
interface ShorteningService {
    fun shorten(url: String): ShortenResult
    fun getOriginalURL(shortURL: String): String?
}

class ShorteningServiceImpl(private val shorteningStrategy: ShorteningStrategy): ShorteningService {
    override fun shorten(url: String): ShortenResult {
        throw Exception("Not implemented")
    }

    override fun getOriginalURL(shortURL: String): String? {
        throw Exception("Not implemented")
    }
}