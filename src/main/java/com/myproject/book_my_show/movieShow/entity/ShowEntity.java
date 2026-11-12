package com.myproject.book_my_show.movieShow.entity;

import com.myproject.book_my_show.event.eventModel.Event;
import com.myproject.book_my_show.screen.entity.ScreenEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_show")
@Getter
@Setter
public class ShowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false)
    private ScreenEntity screen;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void addCreateAt(){
        if(createdAt == null){
            createdAt = LocalDateTime.now();
        }
    }

}


