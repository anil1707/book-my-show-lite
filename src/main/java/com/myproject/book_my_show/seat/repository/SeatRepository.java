package com.myproject.book_my_show.seat.repository;

import com.myproject.book_my_show.seat.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    boolean existsByScreenId(Long screenId);

    List<SeatEntity> findByScreenIdOrderByRowLabelAscSeatNumberAsc(Long screenId);
}
