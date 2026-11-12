package com.myproject.book_my_show.showSeat.repository;

import com.myproject.book_my_show.showSeat.entity.ShowSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeatEntity, Long> {
    @Query("""
        select ss from ShowSeatEntity ss
        join fetch ss.seat s
        where ss.show.id = :showId
        order by s.rowLabel, s.seatNumber
        """)
    List<ShowSeatEntity> findSeatMap(@Param("showId") Long showId);

    @Query("""
        select ss from ShowSeatEntity ss
        join fetch ss.seat
        where ss.show.id = :showId
          and ss.id in :ids
        """)
    List<ShowSeatEntity> findByShowIdAndIdIn(@Param("showId") Long showId,
                                             @Param("ids") List<Long> ids);
}
