package com.myproject.book_my_show.booking.entity;

import com.myproject.book_my_show.booking.enums.BookingStatus;
import com.myproject.book_my_show.movieShow.entity.ShowEntity;
import com.myproject.book_my_show.user.entity.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    private ShowEntity show;

    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CurrentTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}

