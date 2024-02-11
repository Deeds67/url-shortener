# URL Shortener by Pierre Marais

This is a simple URL shortener that hashes URLs using MD5, then encodes the hash to base58. The encoded string is reduced to max 8 characters.

## Usage

### Running the app
```bash
make run
```

### Running the tests
```bash
make test
```

### API documentation
```bash
make run
```
Then navigate to [http://localhost:8080/openapi](http://localhost:8080/openapi)

## Approach explained

As there were no concrete requirements, I kept the implementation as simple as possible, allowing for future expansion.

* I chose to use MD5 as the hashing algorithm, as it is fast and has a low collision rate. (HashCode would be faster, but the chances of collisions are much higher).
* I chose to use a Base58 encoding, as it is compact, and does not contain any characters that could be confused with each other (O, 0, I and l).
* I chose to use PostgreSQL as the database. It should be sufficient until extremely high traffic is reached, after which replication, partitioning, or a NoSQL DB could be considered.
* I chose to limit the shortened URL to 8 characters, as it supports 58^8 unique short URLs (128,063,081,718,016).
* The current approach could generate a collision, but the chance is very low. If a collision is detected, the app will generate a new hash and try again. A future improvement could be to use a Bloom Filter to check for collisions before inserting the URL into the database.

