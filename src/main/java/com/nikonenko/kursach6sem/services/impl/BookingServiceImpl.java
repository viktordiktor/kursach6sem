package com.nikonenko.kursach6sem.services.impl;

import com.nikonenko.kursach6sem.dto.requests.BookingsRequest;
import com.nikonenko.kursach6sem.dto.responses.BookingDto;
import com.nikonenko.kursach6sem.exceptions.BookingNotFoundException;
import com.nikonenko.kursach6sem.models.Booking;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.repositories.BookingRepository;
import com.nikonenko.kursach6sem.services.BookingService;
import com.nikonenko.kursach6sem.services.RecreationObjectService;
import com.nikonenko.kursach6sem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RecreationObjectService recreationObjectService;
    private final ModelMapper modelMapper;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookingDto> getAllBookingsDto() {
        return bookingRepository.findAll().stream()
                .map(booking -> {
                    BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
                    bookingDto.setUserId(booking.getUser().getId());
                    bookingDto.setRecreationObjectId(booking.getRecreationObject().getId());
                    return bookingDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByObjectId(Long objectId) {
        List<BookingDto> bookingDtos = bookingRepository.findAllByRecreationObjectId(objectId).stream()
                .map(booking -> {
                    BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
                    bookingDto.setUserId(booking.getUser().getId());
                    bookingDto.setRecreationObjectId(booking.getRecreationObject().getId());
                    return bookingDto;
                })
                .collect(Collectors.toList());
        return bookingDtos;
    }

    @Override
    public void createBooking(BookingsRequest request, Long objectId) {
        List<LocalDate> dates = correctRequestList(request.getSelectedDates());
        LocalDate currentStartDate = null;
        LocalDate currentEndDate = null;
        for (LocalDate date : dates) {
            if (currentStartDate == null) {
                currentStartDate = date;
                currentEndDate = date;
            } else if (date.isAfter(currentEndDate.plusDays(1).atStartOfDay().toLocalDate())) {
                createBookingInternal(currentStartDate, currentEndDate, objectId);
                currentStartDate = date;
                currentEndDate = date;
            } else {
                currentEndDate = date;
            }
        }
        if (currentStartDate != null) {
            createBookingInternal(currentStartDate, currentEndDate, objectId);
        }
    }

    @Override
    public void closeBooking(Long id) {
        bookingRepository.delete(getOrThrow(id));
    }

    private void createBookingInternal(LocalDate startDate, LocalDate endDate, Long objectId) {
        bookingRepository.save(Booking.builder()
                .bookingStartTime(startDate)
                .bookingEndTime(endDate)
                .recreationObject(modelMapper
                        .map(recreationObjectService.getRecreationObjectById(objectId), RecreationObject.class))
                .user(userService.getCurrentUser())
                .build());
    }

    private List<LocalDate> correctRequestList(List<LocalDate> dates) {
        List<LocalDate> nextDayDates = new ArrayList<>();
        for (LocalDate date : dates) {
            LocalDate nextDay = date.plusDays(1);
            nextDayDates.add(nextDay);
        }
        return nextDayDates.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private Booking getOrThrow(Long id) {
        return bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
    }
}
