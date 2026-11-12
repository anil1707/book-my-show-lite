package com.myproject.book_my_show.movieShow.service;

import com.myproject.book_my_show.common.exception.DuplicateResourceException;
import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.event.eventModel.Event;
import com.myproject.book_my_show.event.eventRepository.EventRepository;
import com.myproject.book_my_show.movieShow.dto.CreateShowRequestDto;
import com.myproject.book_my_show.movieShow.dto.ShowResponseDto;
import com.myproject.book_my_show.movieShow.entity.ShowEntity;
import com.myproject.book_my_show.movieShow.repository.ShowRepository;
import com.myproject.book_my_show.screen.entity.ScreenEntity;
import com.myproject.book_my_show.screen.repository.ScreenRepository;
import com.myproject.book_my_show.seat.SeatType;
import com.myproject.book_my_show.seat.entity.SeatEntity;
import com.myproject.book_my_show.seat.repository.SeatRepository;
import com.myproject.book_my_show.showSeat.dto.ShowSeatMapResponseDto;
import com.myproject.book_my_show.showSeat.dto.ShowSeatResponseDto;
import com.myproject.book_my_show.showSeat.entity.ShowSeatEntity;
import com.myproject.book_my_show.showSeat.enums.ShowSeatStatus;
import com.myproject.book_my_show.showSeat.repository.ShowSeatRepository;
import com.myproject.book_my_show.venue.entity.VenueEntity;
import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final ShowSeatRepository showSeatRepository;
    public ShowService(
            ShowRepository showRepository,
            EventRepository eventRepository,
            ScreenRepository screenRepository,
            SeatRepository seatRepository,
            ShowSeatRepository showSeatRepository
    ){
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.screenRepository = screenRepository;
        this.seatRepository = seatRepository;
        this.showSeatRepository = showSeatRepository;
    }

//    1. create show
    @Transactional
    public ShowResponseDto createShow(CreateShowRequestDto request) {
        Long screenId = request.screenId();
        LocalDateTime startTime = request.startTime();
        LocalDateTime endTime = request.endTime();
//        checking for show overlapping
        if (showRepository.existsOverlappingShow(screenId, startTime, endTime)) {
            throw new DuplicateResourceException("Overlapping with another show");
        }

//        check start time before end time
        if(!request.isEndTimeAfterStartTime()){
            throw new RuntimeException("End time should be after end time");
        }

//        fetching event data
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("No event found of given id"));

//        fetching screen data
        ScreenEntity screen = screenRepository.findById(screenId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No screen found of given id")
                );

//        fetching all seat for that screen
        List<SeatEntity> seats = seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(screenId);

//        show entity generation and save to database
        ShowEntity show = new ShowEntity();
        show.setEvent(event);
        show.setScreen(screen);
        show.setStartTime(request.startTime());
        show.setEndTime(request.endTime());
        show.setBasePrice(request.basePrice());
        ShowEntity showResponse = showRepository.save(show);

//        show seat entity generation and save to database
        List<ShowSeatEntity> showSeats = new ArrayList<>();
        for (SeatEntity seat : seats) {
            ShowSeatEntity ss = new ShowSeatEntity();
            ss.setShow(show);
            ss.setSeat(seat);
            ss.setStatus(ShowSeatStatus.AVAILABLE);
            ss.setPrice(priceFor(seat.getSeatType(), request.basePrice()));
            showSeats.add(ss);
        }
        showSeatRepository.saveAll(showSeats);
        return toResponse(showResponse);
    }

//    2. lists shows for an event
    @Transactional(readOnly = true)
    public List<ShowResponseDto> findShowListByEventId(Long eventId, String city){
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }

        LocalDateTime now = LocalDateTime.now();
        List<ShowEntity> show = (city == null || city.isBlank()) ?
                showRepository.findByEventIdAndStartTimeAfterOrderByStartTimeAsc(eventId, now)
                :
                showRepository.findByEventIdAndScreenVenueCityIgnoreCaseAndStartTimeAfterOrderByStartTimeAsc(
                        eventId, city.trim(), now);
        return show
                .stream()
                .map(this::toResponse)
                .toList();
    }

//    2. show by show Id along with seats
    @Transactional(readOnly = true)
    public ShowSeatMapResponseDto showById(Long id){
        ShowEntity show = showRepository.findWithEventAndScreenById(id)
                .orElseThrow(() -> new ResourceNotFoundException(""));

        List<ShowSeatResponseDto> seats = showSeatRepository.findSeatMap(show.getId())
                .stream()
                .map(this::toSeatResponse)
                .toList();

        return new ShowSeatMapResponseDto(
                show.getId(),
                show.getEvent().getTitle(),
                show.getScreen().getName(),
                show.getStartTime(),
                seats
        );
    }


    private ShowResponseDto toResponse(ShowEntity e){
        VenueEntity venue = e.getScreen().getVenue();
        return new ShowResponseDto(
                e.getId(),
                e.getEvent().getId(),
                e.getEvent().getTitle(),
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                e.getScreen().getId(),
                e.getScreen().getName(),
                e.getStartTime(),
                e.getEndTime(),
                e.getBasePrice()
        );
    }

    private ShowSeatResponseDto toSeatResponse(ShowSeatEntity ss) {
        SeatEntity seat = ss.getSeat();
        return new ShowSeatResponseDto(
                ss.getId(),
                seat.getId(),
                seat.getRowLabel(),
                seat.getSeatNumber(),
                seat.getSeatType(),
                ss.getStatus(),
                ss.getPrice()
        );
    }

    private BigDecimal priceFor(SeatType type, BigDecimal base) {
        return switch (type) {
            case REGULAR -> base;
            case PREMIUM -> base.multiply(new BigDecimal("1.5"));
            case RECLINER -> base.multiply(new BigDecimal("2.0"));
        };
    }
}
