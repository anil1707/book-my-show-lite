package com.myproject.book_my_show.booking.repository;

import com.myproject.book_my_show.booking.entity.BookingEntity;
import com.myproject.book_my_show.booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    // booking history for a user, with everything the response needs
    @EntityGraph(attributePaths = {"show", "show.event", "show.screen", "show.screen.venue"})
    List<BookingEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    // used by the expiry job later
    List<BookingEntity> findByStatusAndExpiresAtBefore(BookingStatus status, LocalDateTime time);

    @EntityGraph(attributePaths = {"show", "show.event", "show.screen", "show.screen.venue"})
    Optional<BookingEntity> findWithDetailsById(Long id);

}
