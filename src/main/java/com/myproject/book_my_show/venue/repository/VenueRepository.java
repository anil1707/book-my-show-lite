package com.myproject.book_my_show.venue.repository;

import com.myproject.book_my_show.venue.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<VenueEntity, Long> {

}
