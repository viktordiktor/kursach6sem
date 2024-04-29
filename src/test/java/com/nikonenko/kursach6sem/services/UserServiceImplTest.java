package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.responses.BookingDto;
import com.nikonenko.kursach6sem.dto.responses.PersonDto;
import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import com.nikonenko.kursach6sem.dto.responses.UserDto;
import com.nikonenko.kursach6sem.models.Booking;
import com.nikonenko.kursach6sem.models.Person;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.models.User;
import com.nikonenko.kursach6sem.repositories.BookingRepository;
import com.nikonenko.kursach6sem.repositories.PersonRepository;
import com.nikonenko.kursach6sem.repositories.UserRepository;
import com.nikonenko.kursach6sem.security.UserDetails;
import com.nikonenko.kursach6sem.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RecreationObjectService recreationObjectService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Person person;
    private UserDto userDto;
    private PersonDto personDto;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
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

        booking = new Booking();
        booking.setId(1L);
        booking.setBookingStartTime(LocalDate.now());
        booking.setBookingEndTime(LocalDate.now().plusDays(1));
        booking.setRecreationObject(RecreationObject.builder().pricePerDay(BigDecimal.ONE).build());
        booking.setUser(user);

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
    void testLoadUserByUsername() {
        doReturn(Optional.of(user)).when(userRepository).findByPhone(anyString());

        UserDetails userDetails = userService.loadUserByUsername("1234567890");

        assertEquals(user.getPhone(), userDetails.getUsername());
    }

    @Test
    void testGetAllUsers() {
        doReturn(List.of(user)).when(userRepository).findAll();
        doReturn(userDto).when(modelMapper).map(any(User.class), eq(UserDto.class));

        List<UserDto> userDtos = userService.getAllUsers();

        assertEquals(1, userDtos.size());
        assertEquals(userDto, userDtos.get(0));
    }

    @Test
    void testAddAdminDashboardAttributes() {
        doReturn(List.of(user)).when(userRepository).findAll();
        doReturn(userDto).when(modelMapper).map(any(User.class), eq(UserDto.class));
        doReturn(List.of(booking)).when(bookingRepository).findAll();

        Model model = mock(Model.class);

        userService.addAdminDashboardAttributes(model);

        verify(model).addAttribute(eq("users"), anyList());
        verify(model).addAttribute(eq("bookings"), anyList());
        verify(model).addAttribute(eq("profit"), anyDouble());
    }

    @Test
    void testAddAdminBookingsAttribute() {
        doReturn(List.of(booking)).when(bookingRepository).findAll();

        Model model = mock(Model.class);

        userService.addAdminBookingsAttribute(model);

        verify(model).addAttribute(eq("bookings"), anyList());
    }

    @Test
    void testAddAdminRecreationObjectsAttribute() {
        doReturn(List.of(new RecreationObjectDto())).when(recreationObjectService).getAllRecreationObjects();

        Model model = mock(Model.class);
        userService.addAdminRecreationObjectsAttribute(model);

        verify(model).addAttribute(eq("objects"), anyList());
    }

    @Test
    void testAddProfileAttributes() {
        doReturn(Optional.of(user)).when(userRepository).findByPhone(anyString());
        doReturn(userDto).when(modelMapper).map(any(User.class), eq(UserDto.class));
        doReturn(personDto).when(modelMapper).map(any(Person.class), eq(PersonDto.class));

        Model model = mock(Model.class);

        userService.addProfileAttributes(model);

        verify(model).addAttribute(eq("user"), any(UserDto.class));
        verify(model).addAttribute(eq("person"), any(PersonDto.class));
        verify(model).addAttribute(eq("bookings"), anyList());
    }

    @Test
    void testUpdateUser() {
        doReturn(Optional.of(user)).when(userRepository).findByPhone(anyString());
        doReturn(Optional.of(person)).when(personRepository).findById(anyLong());
        doReturn(personDto).when(modelMapper).map(any(Person.class), eq(PersonDto.class));
        doReturn(person).when(modelMapper).map(any(PersonDto.class), eq(Person.class));
        doReturn(user).when(modelMapper).map(any(UserDto.class), eq(User.class));

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(1L);
        updatedUserDto.setPhone("1234567890");
        updatedUserDto.setRole("ROLE_ADMIN");
        updatedUserDto.setPassword("password");
        updatedUserDto.setPerson(person);

        userService.updateUser(1L, updatedUserDto);

        verify(userRepository).findByPhone("1234567890");
        verify(userRepository).save(any(User.class));
    }
}