package com.myproject.book_my_show.screen.entity;

import com.myproject.book_my_show.venue.entity.VenueEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "screen")
@Getter
@Setter
public class ScreenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id")
    private VenueEntity venue;


    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;


}
