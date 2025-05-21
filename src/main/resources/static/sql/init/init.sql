DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS consent;

CREATE TABLE IF NOT EXISTS users (
    id UUID,
    email VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS consent (
    id SERIAL,
    user_id UUID NOT NULL,
    consent VARCHAR(255),
    enabled BOOLEAN,
    updated TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
