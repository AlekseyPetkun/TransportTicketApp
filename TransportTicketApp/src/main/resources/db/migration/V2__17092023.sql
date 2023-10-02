CREATE TABLE tickets
(
    id                  SERIAL PRIMARY KEY,
    route               SERIAL REFERENCES routes (id) NOT NULL,
    departure_date_time TIMESTAMP,
    place_number        INTEGER                       NOT NULL,
    price               INTEGER                       NOT NULL,
    buyer_id            BIGINT REFERENCES users (id) DEFAULT NULL,
    reserved            VARCHAR(32)                   NOT NULL -- enum Reserved
);