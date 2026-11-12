CREATE TABLE screen (
                        id         BIGSERIAL PRIMARY KEY,
                        venue_id   BIGINT NOT NULL REFERENCES venue(id),
                        name       VARCHAR(50) NOT NULL,
                        total_seats INTEGER NOT NULL,
                        UNIQUE (venue_id, name)
);

CREATE TABLE seat (
                      id          BIGSERIAL PRIMARY KEY,
                      screen_id   BIGINT NOT NULL REFERENCES screen(id),
                      row_label   VARCHAR(5) NOT NULL,
                      seat_number INTEGER NOT NULL,
                      seat_type   VARCHAR(20) NOT NULL,
                      UNIQUE (screen_id, row_label, seat_number)
);