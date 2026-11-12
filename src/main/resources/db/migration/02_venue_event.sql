CREATE TABLE venue (
                       id          BIGSERIAL PRIMARY KEY,
                       name        VARCHAR(150) NOT NULL,
                       city        VARCHAR(100) NOT NULL,
                       address     VARCHAR(255),
                       created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE event (
                       id            BIGSERIAL PRIMARY KEY,
                       title         VARCHAR(200) NOT NULL,
                       description   TEXT,
                       category      VARCHAR(50) NOT NULL,
                       duration_min  INTEGER,
                       language      VARCHAR(50),
                       created_at    TIMESTAMP NOT NULL DEFAULT now()
);