CREATE TABLE routs
(
    id            SERIAL PRIMARY KEY,
    departure     VARCHAR(64)                     NOT NULL,
    destination   VARCHAR(64)                     NOT NULL,
    carrier       SERIAL REFERENCES carriers (id) NOT NULL,
    durations_min INTEGER                         NOT NULL
);