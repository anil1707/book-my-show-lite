package com.myproject.book_my_show.showSeat.entity;

import com.myproject.book_my_show.movieShow.entity.ShowEntity;
import com.myproject.book_my_show.seat.entity.SeatEntity;
import com.myproject.book_my_show.showSeat.enums.ShowSeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "show_seat")
@Getter
@Setter
public class ShowSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", nullable = false)
    private ShowEntity show;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowSeatStatus status = ShowSeatStatus.AVAILABLE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(nullable = false)
    private Long version= 0L;
}
