package com.myproject.book_my_show.seat.entity;

import com.myproject.book_my_show.screen.entity.ScreenEntity;
import com.myproject.book_my_show.seat.SeatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "seat",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"screen_id", "row_label", "seat_number"}
        )
)
@Getter
@Setter
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private ScreenEntity screen;

    @Column(name = "row_label", nullable = false, length = 5)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

}
