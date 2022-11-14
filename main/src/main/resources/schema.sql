CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR(255)                                        NOT NULL,
    email VARCHAR(512) UNIQUE                                 NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255)                                        NOT NULL
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    lat FLOAT                                               NOT NULL,
    lon FLOAT                                               NOT NULL

);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(2000)                                       NOT NULL,
    category           BIGINT REFERENCES categories (id)                   NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    description        VARCHAR(7000)                                       NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    initiator          BIGINT REFERENCES users (id)                        NOT NULL,
    location           BIGINT REFERENCES locations (id)                    NOT NULL,
    paid               BOOLEAN                                             NOT NULL,
    participant_limit  BIGINT                                              NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    request_moderation BOOLEAN                                             NOT NULL,
    title              VARCHAR(120)                                        NOT NULL,
    state              VARCHAR(20)                                         NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN                                             NOT NULL,
    title  VARCHAR(1000)                                       NOT NULL
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE                         NOT NULL,
    event     BIGINT REFERENCES events (id)                       NOT NULL,
    requester BIGINT REFERENCES users (id)                        NOT NULL,
    status    VARCHAR(20)                                         NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation BIGINT REFERENCES compilations (id) NOT NULL,
    event       BIGINT REFERENCES events (id)       NOT NULL
)

