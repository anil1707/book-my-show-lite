package com.myproject.book_my_show.booking.repository;

import com.myproject.book_my_show.booking.entity.BookingSeatEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeatEntity, Long> {
    @EntityGraph(attributePaths = {"showSeat", "showSeat.seat"})
    List<BookingSeatEntity> findByBookingId(Long bookingId);

    @Query("""
        select bs from BookingSeatEntity bs
        join fetch bs.showSeat
        where bs.booking.id in :bookingIds
        """)
    List<BookingSeatEntity> findByBookingIdIn(@Param("bookingIds") List<Long> bookingIds);
}
