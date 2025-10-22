-- Update users table
ALTER TABLE users
    ADD COLUMN username VARCHAR(255) NOT NULL,
    ADD COLUMN provider VARCHAR(50) NOT NULL DEFAULT 'LOCAL',
    ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'ROLE_USER',
    ADD COLUMN password VARCHAR(255) NOT NULL;

ALTER TABLE users
    DROP COLUMN password_hash;

-- Adjust unique constraint
ALTER TABLE users
    DROP CONSTRAINT IF EXISTS users_email_key;

ALTER TABLE users
    ADD CONSTRAINT users_email_provider_unique UNIQUE (email, provider);

-- Create refresh_tokens table
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);