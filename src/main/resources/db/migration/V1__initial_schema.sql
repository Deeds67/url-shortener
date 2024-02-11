CREATE TABLE IF NOT EXISTS shortened_url (
    id BIGSERIAL PRIMARY KEY,
    original_url text NOT NULL,
    short_url VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_shortenedurl_short_url ON shortened_url (short_url);
CREATE INDEX IF NOT EXISTS idx_shortenedurl_original_url ON shortened_url (original_url);

