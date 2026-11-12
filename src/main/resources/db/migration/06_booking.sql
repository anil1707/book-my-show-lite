CREATE TABLE booking (
                         id            BIGSERIAL PRIMARY KEY,
                         user_id       BIGINT NOT NULL REFERENCES app_user(id),
                         show_id       BIGINT NOT NULL REFERENCES movie_show(id),
                         status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                         total_amount  NUMERIC(10,2) NOT NULL,
                         expires_at    TIMESTAMP NOT NULL,
                         created_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE booking_seat (
                              id            BIGSERIAL PRIMARY KEY,
                              booking_id    BIGINT NOT NULL REFERENCES booking(id),
                              show_seat_id  BIGINT NOT NULL REFERENCES show_seat(id),
                              price         NUMERIC(10,2) NOT NULL,
                              UNIQUE (booking_id, show_seat_id)
);