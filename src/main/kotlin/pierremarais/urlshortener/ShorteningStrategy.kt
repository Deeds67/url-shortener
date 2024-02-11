package pierremarais.urlshortener

import java.math.BigInteger
import java.security.MessageDigest

interface ShorteningStrategy {
    fun shorten(value: String): String
}

class Base58Strategy: ShorteningStrategy {
    private val base58Chars = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"

    override fun shorten(value: String): String {
        val hash = md5(value)
        val hashInt = BigInteger(1, hash)

        val result = StringBuilder()
        var num = hashInt
        while (num > BigInteger.ZERO) {
            val remainder = num.mod(BigInteger.valueOf(58))
            result.insert(0, base58Chars[remainder.toInt()])
            num /= BigInteger.valueOf(58)
        }

        val shortenedString = result.toString()

        // Truncate the base58 string if it exceeds 8 characters
        return if (shortenedString.length > 8) shortenedString.substring(0, 8) else shortenedString
    }

    private fun md5(input: String): ByteArray {
        val digest = MessageDigest.getInstance("MD5")
        return digest.digest(input.toByteArray())
    }
}