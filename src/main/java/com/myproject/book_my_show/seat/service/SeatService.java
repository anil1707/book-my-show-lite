package com.myproject.book_my_show.seat.service;

import com.myproject.book_my_show.common.exception.DuplicateResourceException;
import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.screen.entity.ScreenEntity;
import com.myproject.book_my_show.screen.repository.ScreenRepository;
import com.myproject.book_my_show.seat.dto.GenerateSeatsRequestDto;
import com.myproject.book_my_show.seat.dto.SeatResponseDto;
import com.myproject.book_my_show.seat.entity.SeatEntity;
import com.myproject.book_my_show.seat.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;

    public SeatService(ScreenRepository screenRepository, SeatRepository seatRepository){
        this.seatRepository = seatRepository;
        this.screenRepository = screenRepository;

    }

    @Transactional
    public List<SeatResponseDto> generateSeat(GenerateSeatsRequestDto request){
        Long screenId = request.screenId();

        ScreenEntity screen = screenRepository.findById(screenId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Screen not found for given id")
                );

        if(seatRepository.existsByScreenId(screenId)){
            throw new DuplicateResourceException("Seats already exist for this screen");
        }

        List<SeatEntity> seats = new ArrayList<>();
        for(int r = 0;r<request.rows(); r++){
            String rowLabel = String.valueOf((char) ('A' + r));
            for(int n=1;n<=request.seatsPerRow();n++){
                SeatEntity seat = new SeatEntity();
                seat.setScreen(screen);
                seat.setRowLabel(rowLabel);
                seat.setSeatNumber(n);
                seat.setSeatType(request.seatType());
                seats.add(seat);
            }
        }

        List<SeatEntity> response = seatRepository.saveAll(seats);
        screen.setTotalSeats(request.rows()*request.seatsPerRow());
        return response.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SeatResponseDto> seatByScreenId(Long id){
        List<SeatEntity> response = seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(id);
        return response.stream()
                .map(this::toResponse)
                .toList();
    }

    public SeatResponseDto seatById(Long id){
        SeatEntity seat = seatRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Seat not found with given id")
                );

        return toResponse(seat);
    }


    private SeatResponseDto toResponse(SeatEntity s) {
        return new SeatResponseDto(
                s.getId(),
                s.getScreen().getId(),
                s.getRowLabel(),
                s.getSeatNumber(),
                s.getSeatType()
        );
    }
}
