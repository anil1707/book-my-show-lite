package com.myproject.book_my_show.event.service;

import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.event.dto.EventRequestDto;
import com.myproject.book_my_show.event.dto.EventResponseDto;
import com.myproject.book_my_show.event.eventModel.Event;
import com.myproject.book_my_show.event.eventRepository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public List<EventResponseDto> findAllEvents(){
        List<Event> response = eventRepository.findAll();
        System.out.println(response);

        return response.stream()
                .map(this::toResponse)
                .toList();

    }

    @Transactional(readOnly = true)
    public EventResponseDto findById(Long id){
        Event response =  eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No event found with given id"));

        return toResponse(response);
    }

    @Transactional
    public EventResponseDto addEvent(EventRequestDto request) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setCategory(request.category());
        event.setDurationMin(request.durationMin());
        event.setLanguage(request.language());
        Event response =  eventRepository.save(event);
        return toResponse(response);
    }

    private EventResponseDto toResponse(Event e){
        return new EventResponseDto(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getCategory(),
                e.getDurationMin(),
                e.getLanguage(),
                e.getCreatedAt()
        );
    }

}
