package com.myproject.book_my_show.booking.entity;

import com.myproject.book_my_show.showSeat.entity.ShowSeatEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(name = "booking_seat")
public class BookingSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_seat_id", nullable = false)
    private ShowSeatEntity showSeat;



    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

}
