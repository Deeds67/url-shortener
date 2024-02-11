package pierremarais.urlshortener

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
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

class PostgresShortenedURLRepository(dbConfig: pierremarais.DatabaseConfig) : ShortenedURLRepository {
    object ShortenedURLTable : Table("shortened_url") {
        val shortURL: Column<String> = varchar("short_url", 10).index(isUnique = true)
        val originalURL: Column<String> = text("original_url").index()
    }

    init {
        val url = "jdbc:postgresql://${dbConfig.host}:${dbConfig.port}/${dbConfig.database}"
        Database.connect(url, user = dbConfig.user, password = dbConfig.password)
    }

    override fun save(shortenedURL: ShortenedURL) {
        transaction {
            ShortenedURLTable.insert {
                it[shortURL] = shortenedURL.shortURL
                it[originalURL] = shortenedURL.originalURL
            }
        }
    }

    override fun findByShortenedURL(shortURL: String): ShortenedURL? =
        transaction {
            ShortenedURLTable.select(ShortenedURLTable.originalURL).where { ShortenedURLTable.shortURL eq shortURL }.map {
                ShortenedURL(it[ShortenedURLTable.originalURL], shortURL)
            }.firstOrNull()
        }

    override fun findByOriginalURL(originalURL: String): ShortenedURL? =
        transaction {
            ShortenedURLTable.select(ShortenedURLTable.shortURL).where { ShortenedURLTable.originalURL eq originalURL }.map {
                ShortenedURL(originalURL, it[ShortenedURLTable.shortURL])
            }.firstOrNull()
        }
}