package com.myproject.book_my_show.event.eventModel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 200)
    private String title;

    private String description;

    @Column(nullable = false)
    private String category;

    @Column(name = "duration_min")
    private Integer durationMin;

    @Column(length = 50)
    private String language;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }



}
