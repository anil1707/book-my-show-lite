package com.myproject.book_my_show.movieShow.repository;

import com.myproject.book_my_show.movieShow.entity.ShowEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<ShowEntity, Long> {
    @Query("""
        select count(s) > 0 from ShowEntity s
        where s.screen.id = :screenId
          and s.startTime < :endTime
          and s.endTime > :startTime
        """)
    boolean existsOverlappingShow(@Param("screenId") Long screenId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    @EntityGraph(attributePaths = {"event", "screen", "screen.venue"})
    List<ShowEntity> findByEventIdAndStartTimeAfterOrderByStartTimeAsc(
            Long eventId, LocalDateTime now);

    @EntityGraph(attributePaths = {"event", "screen", "screen.venue"})
    List<ShowEntity> findByEventIdAndScreenVenueCityIgnoreCaseAndStartTimeAfterOrderByStartTimeAsc(
            Long eventId, String city, LocalDateTime now);

    @EntityGraph(attributePaths = {"event", "screen"})
    Optional<ShowEntity> findWithEventAndScreenById(Long id);

}
