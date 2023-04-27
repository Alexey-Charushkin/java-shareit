/*
drop table IF EXISTS users;
drop table IF EXISTS requests;
drop table IF EXISTS items;
drop table IF EXISTS bookings;
drop table IF EXISTS comments;
*/

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name  varchar(100),
    email varchar(320),
    UNIQUE (id, email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    description  VARCHAR(1000),
    requestor_id BIGINT,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requestor_id) REFERENCES users (id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name        VARCHAR(100),
    description VARCHAR(1000),
    is_avaible  VARCHAR(100),
    owner_id    BIGINT,
    reqest_id   BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT fk_items_to_requests FOREIGN KEY (reqest_id) REFERENCES requests (id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     VARCHAR(100),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY (booker_id) REFERENCES users (id),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    text     VARCHAR(1000),
    item_id  BIGINT,
    autor_id BIGINT,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (autor_id) REFERENCES users (id),
    UNIQUE (id)
);