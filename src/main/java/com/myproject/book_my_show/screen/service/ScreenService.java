package com.myproject.book_my_show.screen.service;

import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.screen.dto.CreateScreenRequest;
import com.myproject.book_my_show.screen.dto.ScreenResponse;
import com.myproject.book_my_show.screen.entity.ScreenEntity;
import com.myproject.book_my_show.screen.repository.ScreenRepository;
import com.myproject.book_my_show.venue.entity.VenueEntity;
import com.myproject.book_my_show.venue.repository.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScreenService {
    Logger log = LoggerFactory.getLogger(ScreenService.class);
    private final ScreenRepository screenRepository;
    private final VenueRepository venueRepository;

    public ScreenService(
            ScreenRepository screenRepository,
            VenueRepository venueRepository
    ){
        this.screenRepository = screenRepository;
        this.venueRepository = venueRepository;
    }

//    1. add screen
    @Transactional
    public ScreenResponse addScreen(CreateScreenRequest request){
        VenueEntity venue = venueRepository.findById(request.venueId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Venue not found with given id")
                );
        ScreenEntity entity = new ScreenEntity();
        entity.setName(request.name());
        entity.setVenue(venue);
        entity.setTotalSeats(request.totalSeats());

        ScreenEntity response = screenRepository.save(entity);

        return toResponse(response);

    }
//    2. get screen by id
    public ScreenResponse findById(Long id){
        ScreenEntity response = screenRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Screen not found with given id")
                );
        return toResponse(response);
    }

//    3. find all screen by venue id
    @Transactional(readOnly = true)
    public List<ScreenResponse> findScreensByVenueId(Long id){
        log.info(String.valueOf(id));
        List<ScreenEntity> screens = screenRepository.findByVenueId(id);
        return screens.stream()
                .map(this::toResponse)
                .toList();
    }

//    to map screen response dto
    private ScreenResponse toResponse(ScreenEntity e){
        return new ScreenResponse(
                e.getId(),
                e.getVenue().getId(),
                e.getVenue().getName(),
                e.getName(),
                e.getTotalSeats()
        );
    }
}
