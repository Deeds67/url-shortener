package pierremarais.urlshortener

interface ShorteningStrategy {
    fun shorten(value: String): String
}

class Base58Strategy: ShorteningStrategy {
    override fun shorten(value: String): String {
        return value.take(8)
    }
}