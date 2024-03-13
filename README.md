# URL Shortener

This is a simple URL shortener that hashes URLs using MD5, then encodes the hash with base58. The encoded string is reduced to max 8 characters. The task was to develop a solution within 3 hours.

## Usage

### Running the app
```bash
make run
```

### Running the tests
```bash
make test
```

These commands can be viewed in the `Makefile` for more details.

### API documentation

The api is described in `src/main/resources/openapi/documentation.yaml`.

It can be viewed in a browser by running the app and navigating to [http://localhost:8080/openapi](http://localhost:8080/openapi)

## Approach explained

As there were no concrete requirements, I kept the implementation as simple as possible, allowing for future expansion.

* I chose to use **MD5** as the hashing algorithm, as it is fast and has a low collision rate. (HashCode would be faster, but the chances of collisions are much higher).
* I chose to use a **Base58 encoding**, as it is compact, and does not contain any characters that could be confused with each other (O, 0, I and l).
* I chose to limit the shortened URL to **8 characters**, as it supports 58^8 unique short URLs (128,063,081,718,016).
* I chose to use **PostgreSQL** as the database. **Flyway** is used to do DB migrations. It should be sufficient until extremely high traffic is reached, after which replication, partitioning, or a NoSQL DB could be considered.
* The current approach could generate a collision, but the chance is very low. If a collision is detected, the app will generate a new hash and try again. A future improvement could be to use a **Bloom Filter** to check for collisions before inserting the URL into the database.

## Testing explained

There are 3 types of tests:
* **Unit tests** - These test the individual components of the app.
* **Integration tests** - These test the app's interaction with the database & request/response serialization + deserialization.
* **Functional tests** - These test the app's functionality as a whole without integrating with a DB.

## Monitoring

* The app exposes Dropwizard metrics in log format (latency percentiles, http status code counts, etc). These can be collected by a log aggregator such as ELK and used to create dashboards and configure alerting.


### Example usage

```bash
curl -XPUT "http://localhost:8080/shortened-urls" -H "Content-Type: application/json" -d '{"url": "https://www.google.com"}'
```

Response:
```json
{ "short_url" : "http://localhost:8080/iMhpsFwa" }
```
