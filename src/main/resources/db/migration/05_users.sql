CREATE TABLE app_user (
                          id          BIGSERIAL PRIMARY KEY,
                          name        VARCHAR(100),
                          email       VARCHAR(150) NOT NULL UNIQUE,
                          role        VARCHAR(20) NOT NULL DEFAULT 'USER',
                          created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE otp_token (
                           id          BIGSERIAL PRIMARY KEY,
                           email       VARCHAR(150) NOT NULL,
                           otp_hash    VARCHAR(100) NOT NULL,
                           expires_at  TIMESTAMP NOT NULL,
                           attempts    INTEGER NOT NULL DEFAULT 0,
                           consumed    BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_otp_email_created ON otp_token (email, created_at DESC);