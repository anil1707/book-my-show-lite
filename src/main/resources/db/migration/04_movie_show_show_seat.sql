CREATE TABLE show (
                      id          BIGSERIAL PRIMARY KEY,
                      event_id    BIGINT NOT NULL REFERENCES event(id),
                      screen_id   BIGINT NOT NULL REFERENCES screen(id),
                      start_time  TIMESTAMP NOT NULL,
                      end_time    TIMESTAMP NOT NULL,
                      base_price  NUMERIC(10,2) NOT NULL,
                      created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE show_seat (
                           id           BIGSERIAL PRIMARY KEY,
                           show_id      BIGINT NOT NULL REFERENCES show(id),
                           seat_id      BIGINT NOT NULL REFERENCES seat(id),
                           status       VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
                           price        NUMERIC(10,2) NOT NULL,
                           locked_until TIMESTAMP,
                           version      BIGINT NOT NULL DEFAULT 0,
                           UNIQUE (show_id, seat_id)
);