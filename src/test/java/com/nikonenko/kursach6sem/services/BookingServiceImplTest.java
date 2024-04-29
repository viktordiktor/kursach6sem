package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.requests.BookingsRequest;
import com.nikonenko.kursach6sem.dto.responses.BookingDto;
import com.nikonenko.kursach6sem.dto.responses.PersonDto;
import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import com.nikonenko.kursach6sem.dto.responses.UserDto;
import com.nikonenko.kursach6sem.models.Booking;
import com.nikonenko.kursach6sem.models.Person;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.models.User;
import com.nikonenko.kursach6sem.repositories.BookingRepository;
import com.nikonenko.kursach6sem.repositories.UserRepository;
import com.nikonenko.kursach6sem.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private RecreationObjectService recreationObjectService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Person person;
    private UserDto userDto;
    private PersonDto personDto;
    private Booking booking;
    private BookingDto bookingDto;
    private RecreationObject recreationObject;
    private RecreationObjectDto recreationObjectDto;

    @BeforeEach
    public void init() {
        user = new User();
        user.setId(1L);
        user.setPhone("1234567890");
        user.setRole("ROLE_USER");

        person = new Person();
        person.setUserId(1L);
        person.setName("John");
        person.setGender("Male");
        person.setUser(user);
        user.setPerson(person);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPhone("1234567890");
        userDto.setRole("ROLE_USER");
        userDto.setPerson(person);

        userDto.setBookings(Set.of(new Booking()));

        personDto = new PersonDto();
        personDto.setUserId(1L);
        personDto.setName("John");
        personDto.setGender("Male");
        personDto.setUser(user);

        recreationObject = RecreationObject.builder()
                .pricePerDay(BigDecimal.ONE)
                .build();

        recreationObjectDto = RecreationObjectDto.builder()
                .pricePerDay(BigDecimal.ONE)
                .build();


        booking = new Booking();
        booking.setId(1L);
        booking.setBookingStartTime(LocalDate.now());
        booking.setBookingEndTime(LocalDate.now().plusDays(1));
        booking.setRecreationObject(RecreationObject.builder().pricePerDay(BigDecimal.ONE).build());
        booking.setUser(user);
        booking.setRecreationObject(recreationObject);

        recreationObject.setBookings(Set.of(booking));
        recreationObjectDto.setBookings(Set.of(booking));

        user.setBookings(Set.of(booking));
        userDto.setBookings(Set.of(booking));

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBookingStartTime(LocalDate.now());
        bookingDto.setBookingEndTime(LocalDate.now().plusDays(1));

        Authentication authentication = new UsernamePasswordAuthenticationToken("1234567890", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetAllBookings() {
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(bookings, result);
    }

    @Test
    public void testGetAllBookingsDto() {
        List<Booking> bookings = Arrays.asList(booking, booking);
        doReturn(bookingDto).when(modelMapper).map(any(Booking.class), eq(BookingDto.class));
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<BookingDto> result = bookingService.getAllBookingsDto();

        assertEquals(2, result.size());
        assertEquals(booking.getUser().getId(), result.get(0).getUserId());
        assertEquals(booking.getRecreationObject().getId(), result.get(0).getRecreationObjectId());
        assertEquals(booking.getUser().getId(), result.get(1).getUserId());
        assertEquals(booking.getRecreationObject().getId(), result.get(1).getRecreationObjectId());
    }

    @Test
    public void testGetBookingsByObjectId() {
        List<Booking> bookings = Arrays.asList(booking, booking);
        doReturn(bookingDto).when(modelMapper).map(any(Booking.class), eq(BookingDto.class));
        when(bookingRepository.findAllByRecreationObjectId(anyLong())).thenReturn(bookings);

        List<BookingDto> result = bookingService.getBookingsByObjectId(1L);

        assertEquals(2, result.size());
        assertEquals(booking.getRecreationObject().getId(), result.get(0).getRecreationObjectId());
        assertEquals(booking.getRecreationObject().getId(), result.get(1).getRecreationObjectId());
    }

    @Test
    public void testCreateBooking() {
        BookingsRequest request = new BookingsRequest();
        request.setSelectedDates(Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1)));

        when(recreationObjectService.getRecreationObjectById(anyLong())).thenReturn(recreationObjectDto);
        doReturn(recreationObject).when(modelMapper).map(any(RecreationObjectDto.class), eq(RecreationObject.class));
        when(userService.getCurrentUser()).thenReturn(new User());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        bookingService.createBooking(request, 1L);

        assertEquals(1, bookingRepository.findAll().size());
    }

    @Test
    public void testCloseBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(new Booking()));

        bookingService.closeBooking(1L);

        assertEquals(0, bookingRepository.findAll().size());
    }
}
