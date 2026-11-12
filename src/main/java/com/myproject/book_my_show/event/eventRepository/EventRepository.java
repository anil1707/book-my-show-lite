package com.myproject.book_my_show.event.eventRepository;

import com.myproject.book_my_show.event.eventModel.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
