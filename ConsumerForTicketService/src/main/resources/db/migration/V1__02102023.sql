CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(64)   NOT NULL,
    last_name  VARCHAR(64)   NOT NULL
);

CREATE TABLE carriers
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(64)   NOT NULL UNIQUE,
    phone      VARCHAR(32)   NOT NULL UNIQUE
);

CREATE TABLE routs
(
    id            SERIAL PRIMARY KEY,
    departure     VARCHAR(64)                     NOT NULL,
    destination   VARCHAR(64)                     NOT NULL,
    carrier       SERIAL REFERENCES carriers (id) NOT NULL,
    durations_min INTEGER                         NOT NULL
);

CREATE TABLE tickets
(
    id                  SERIAL PRIMARY KEY,
    route               SERIAL REFERENCES routs (id) NOT NULL,
    departure_date_time TIMESTAMP,
    place_number        INTEGER                       NOT NULL,
    price               INTEGER                       NOT NULL,
    buyer_id            BIGINT REFERENCES users (id) DEFAULT NULL
);