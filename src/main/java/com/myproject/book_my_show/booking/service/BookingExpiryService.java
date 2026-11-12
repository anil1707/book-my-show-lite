package com.myproject.book_my_show.booking.service;

import com.myproject.book_my_show.booking.entity.BookingEntity;
import com.myproject.book_my_show.booking.entity.BookingSeatEntity;
import com.myproject.book_my_show.booking.enums.BookingStatus;
import com.myproject.book_my_show.booking.repository.BookingRepository;
import com.myproject.book_my_show.booking.repository.BookingSeatRepository;
import com.myproject.book_my_show.showSeat.entity.ShowSeatEntity;
import com.myproject.book_my_show.showSeat.enums.ShowSeatStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service

public class BookingExpiryService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public BookingExpiryService(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository
    ){
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void releaseExpiredBookings(){
        List<BookingEntity> expired = bookingRepository.findByStatusAndExpiresAtBefore(BookingStatus.PENDING, LocalDateTime.now());

        if (expired.isEmpty()) {
            return;
        }

        List<Long> ids = expired.stream().map(BookingEntity::getId).toList();

        for(BookingSeatEntity bs : bookingSeatRepository.findByBookingIdIn(ids)){
            ShowSeatEntity ss = bs.getShowSeat();

            // only release if still locked (never touch BOOKED seats)
            if(ss.getStatus() == ShowSeatStatus.LOCKED && ss.getLockedUntil() != null
                    && !ss.getLockedUntil().isAfter(LocalDateTime.now())){
                ss.setStatus(ShowSeatStatus.AVAILABLE);
                ss.setLockedUntil(null);
            }
        }

        expired.forEach(booking -> booking.setStatus(BookingStatus.EXPIRED));

        log.info("Expired {} booking(s)", expired.size());
    }
}
