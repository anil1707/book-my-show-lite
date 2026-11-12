package com.myproject.book_my_show.venue.Service;

import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.common.response.ApiResponse;
import com.myproject.book_my_show.venue.dto.CreateVenueRequest;
import com.myproject.book_my_show.venue.dto.VenueResponse;
import com.myproject.book_my_show.venue.entity.VenueEntity;
import com.myproject.book_my_show.venue.repository.VenueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository){
        this.venueRepository = venueRepository;
    }


//    1. add venue
    public VenueResponse addVenue(CreateVenueRequest request){
        VenueEntity venue = new VenueEntity();
        venue.setName(request.name());
        venue.setCity(request.city());
        venue.setAddress(request.address());
        VenueEntity response = venueRepository.save(venue);

        return toVenueResponse(response);
    }

//    2. get venue by id
    public VenueResponse venueById(Long id){
        VenueEntity response = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No venue found with given id"));
        return toVenueResponse(response);
    }
//    3. get all venue
    public List<VenueResponse> findAllVenue(){
        return venueRepository.findAll()
                .stream()
                .map(this::toVenueResponse)
                .toList();
    }

    private VenueResponse toVenueResponse(VenueEntity venue){
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getAddress(),
                venue.getCreatedAt()
        );
    }
}
