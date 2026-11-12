package com.myproject.book_my_show.screen.repository;

import com.myproject.book_my_show.screen.entity.ScreenEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<ScreenEntity, Long> {
    @EntityGraph(attributePaths = "venue")
    List<ScreenEntity> findByVenueId(Long id);
}
