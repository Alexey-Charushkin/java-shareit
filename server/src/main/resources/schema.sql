drop table IF EXISTS users CASCADE;
drop table IF EXISTS requests CASCADE;
drop table IF EXISTS items CASCADE;
drop table IF EXISTS bookings CASCADE;
drop table IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE,
    name  varchar(100),
    email varchar(320) UNIQUE
);

CREATE TABLE IF NOT EXISTS requests
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE,
    description  VARCHAR(1000),
    requestor_id BIGINT,
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE,
    name         VARCHAR(100),
    description  VARCHAR(1000),
    is_available VARCHAR(100),
    owner_id     BIGINT,
    request_id   BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT fk_items_to_requests FOREIGN KEY (request_id) REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT                                          NOT NULL,
    booker_id  BIGINT                                          NOT NULL,
    status     VARCHAR(100),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL UNIQUE,
    text      VARCHAR(1000),
    item_id   BIGINT                                          NOT NULL,
    author_id BIGINT                                          NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id)
);