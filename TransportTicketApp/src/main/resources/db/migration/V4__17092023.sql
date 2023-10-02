CREATE TABLE carriers
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(64)   NOT NULL UNIQUE,
    phone      VARCHAR(32)   NOT NULL UNIQUE
);