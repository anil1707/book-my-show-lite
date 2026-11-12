package com.myproject.book_my_show.booking.service;

import com.myproject.book_my_show.booking.dto.BookingResponseDto;
import com.myproject.book_my_show.booking.dto.LockSeatsRequestDto;
import com.myproject.book_my_show.booking.entity.BookingEntity;
import com.myproject.book_my_show.booking.entity.BookingSeatEntity;
import com.myproject.book_my_show.booking.enums.BookingStatus;
import com.myproject.book_my_show.booking.repository.BookingRepository;
import com.myproject.book_my_show.booking.repository.BookingSeatRepository;
import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.common.exception.SeatUnavailableException;
import com.myproject.book_my_show.movieShow.entity.ShowEntity;
import com.myproject.book_my_show.movieShow.repository.ShowRepository;
import com.myproject.book_my_show.showSeat.entity.ShowSeatEntity;
import com.myproject.book_my_show.showSeat.enums.ShowSeatStatus;
import com.myproject.book_my_show.showSeat.repository.ShowSeatRepository;
import com.myproject.book_my_show.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    @Value("${seat.locking-time}")
    private  int LOCK_MINUTES;

    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;


    public BookingService(
            ShowRepository showRepository,
            ShowSeatRepository showSeatRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository
    ){
        this.showRepository = showRepository;
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }


    @Transactional
    public BookingResponseDto lockSeat(Long userId, LockSeatsRequestDto request){

        ShowEntity show = showRepository.findWithEventAndScreenById(request.showId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Show not found for give id: " + request.showId())
                );

        if(!show.getStartTime().isAfter(LocalDateTime.now())){
            throw new SeatUnavailableException("This show has already started");
        }

        List<Long> ids = request.showSeatIds().stream().distinct().toList();
        List<ShowSeatEntity> seats = showSeatRepository.findByShowIdAndIdIn(show.getId(), ids);

        if (seats.size() != ids.size()) {
            throw new ResourceNotFoundException("One or more seats do not belong to this show");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lockUntil = now.plusMinutes(LOCK_MINUTES);

        // 1. every seat must be free, or have an expired lock
        for(ShowSeatEntity seat : seats){
            boolean free = seat.getStatus() == ShowSeatStatus.AVAILABLE ||
                    (seat.getStatus() == ShowSeatStatus.LOCKED && seat.getLockedUntil() != null && seat.getLockedUntil().isBefore(now));

            if(!free){
                throw new SeatUnavailableException("Seat is no longer available");
            }
        }

        // 2. lock them (the @Version column protects against two users doing this at once)

        BigDecimal total = BigDecimal.ZERO;
        for(ShowSeatEntity ss : seats){
            ss.setStatus(ShowSeatStatus.LOCKED);
            ss.setLockedUntil(lockUntil);
            total = total.add(ss.getPrice());
        }

        // 3. create the pending booking
        BookingEntity booking = new BookingEntity();
        booking.setUser(userRepository.getReferenceById(userId));
        booking.setStatus(BookingStatus.PENDING);
        booking.setShow(show);
        booking.setTotalAmount(total);
        booking.setExpiresAt(lockUntil);

       bookingRepository.save(booking);

       List<BookingSeatEntity> bookingSeats = new ArrayList<>();

       for(ShowSeatEntity ss : seats){
           BookingSeatEntity bs = new BookingSeatEntity();
           bs.setBooking(booking);
           bs.setShowSeat(ss);
           bs.setPrice(ss.getPrice());
           bookingSeats.add(bs);

       }

       bookingSeatRepository.saveAll(bookingSeats);

       return toResponse(booking, bookingSeats);

       }

       @Transactional
       public BookingResponseDto confirmBooking(Long userId, Long bookingId){
           BookingEntity booking = bookingRepository.findWithDetailsById(bookingId)
                   .orElseThrow(() -> new ResourceNotFoundException(
                           "Booking not found with id " + bookingId));

           // not your booking: respond as if it doesn't exist (IDos)
           if (!booking.getUser().getId().equals(userId)) {
               throw new ResourceNotFoundException("Booking not found with id " + bookingId);
           }

           if (booking.getStatus() != BookingStatus.PENDING) {
               throw new SeatUnavailableException(
                       "Booking is already " + booking.getStatus());
           }

           LocalDateTime now = LocalDateTime.now();

           if (booking.getExpiresAt().isBefore(now)) {
               throw new SeatUnavailableException("Booking has expired. Please select seats again");
           }

           List<BookingSeatEntity> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);

           // every seat must still be locked (an expired-then-relocked seat must not be confirmed)
           for(BookingSeatEntity bs : bookingSeats){
               ShowSeatEntity ss = bs.getShowSeat();
               if(ss.getStatus() != ShowSeatStatus.LOCKED ||
                       ss.getLockedUntil() == null ||
                       !ss.getLockedUntil().equals(booking.getExpiresAt())
               ){
                   throw new SeatUnavailableException("Seat lock is no longer valid");
               }
           }

           for(BookingSeatEntity bs : bookingSeats){
               ShowSeatEntity ss = bs.getShowSeat();
               ss.setStatus(ShowSeatStatus.BOOKED);
               ss.setLockedUntil(null);
           }

           booking.setStatus(BookingStatus.CONFIRMED);

           return toResponse(booking, bookingSeats);
       }

   private BookingResponseDto toResponse(BookingEntity booking, List<BookingSeatEntity> bookingSeat){
    List<String> seats = bookingSeat.stream()
            .map(seat -> seat.getShowSeat().getSeat().getRowLabel() + seat.getShowSeat().getSeat().getSeatNumber())
            .toList();

        return new BookingResponseDto(
                booking.getId(),
                booking.getStatus(),
                booking.getShow().getEvent().getTitle(),
                booking.getShow().getScreen().getVenue().getName(),
                booking.getShow().getScreen().getName(),
                booking.getShow().getStartTime(),
                seats,
                booking.getTotalAmount(),
                booking.getExpiresAt()

        );
   }


}
