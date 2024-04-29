package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.ReviewDto;
import com.nikonenko.kursach6sem.dto.requests.BookingsRequest;
import com.nikonenko.kursach6sem.dto.responses.BookingDto;
import com.nikonenko.kursach6sem.models.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();

    List<BookingDto> getAllBookingsDto();

    List<BookingDto> getBookingsByObjectId(Long objectId);

    void createBooking(BookingsRequest request, Long objectId);

    void closeBooking(Long id);
}
