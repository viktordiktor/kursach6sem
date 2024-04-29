package com.nikonenko.kursach6sem.services.impl;

import com.nikonenko.kursach6sem.dto.responses.BookingDto;
import com.nikonenko.kursach6sem.dto.responses.PersonDto;
import com.nikonenko.kursach6sem.dto.responses.UserDto;
import com.nikonenko.kursach6sem.exceptions.PersonNotFoundException;
import com.nikonenko.kursach6sem.models.Booking;
import com.nikonenko.kursach6sem.models.Person;
import com.nikonenko.kursach6sem.models.User;
import com.nikonenko.kursach6sem.repositories.BookingRepository;
import com.nikonenko.kursach6sem.repositories.PersonRepository;
import com.nikonenko.kursach6sem.repositories.UserRepository;
import com.nikonenko.kursach6sem.services.BookingService;
import com.nikonenko.kursach6sem.services.RecreationObjectService;
import com.nikonenko.kursach6sem.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.nikonenko.kursach6sem.security.UserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final BookingRepository bookingRepository;
    private final RecreationObjectService recreationObjectService;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByPhone(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found!");
        return new UserDetails(user.get());
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addAdminDashboardAttributes(Model model) {
        model.addAttribute("users", getAllUsers());
        model.addAttribute("bookings", bookingRepository.findAll());
        model.addAttribute("profit", bookingRepository.findAll().stream()
                .mapToDouble(booking ->
                        booking.getRecreationObject().getPricePerDay().doubleValue()
                                * -ChronoUnit.DAYS.between(booking.getBookingEndTime(), booking.getBookingStartTime()))
                .sum());
    }

    @Override
    public void addAdminBookingsAttribute(Model model) {
        model.addAttribute("bookings", bookingRepository.findAll());
    }

    @Override
    public void addAdminRecreationObjectsAttribute(Model model) {
        model.addAttribute("objects", recreationObjectService.getAllRecreationObjects());
    }

    @Override
    public void addProfileAttributes(Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", modelMapper.map(user, UserDto.class));
        model.addAttribute("person", modelMapper.map(user.getPerson(), PersonDto.class));
        model.addAttribute("bookings", user.getBookings().stream()
                .sorted(Comparator.comparing(Booking::getBookingStartTime))
                .collect(Collectors.toList()));
    }

    @Override
    public void updateUser(Long id, UserDto updatedUser) {
        User user = getCurrentUser();
        Person person = modelMapper.map(getPersonByUserId(id), Person.class);
        updatedUser.setRole(user.getRole());
        updatedUser.setPerson(person);
        updatedUser.setId(user.getId());
        updatedUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        userRepository.save(modelMapper.map(updatedUser, User.class));
    }

    @Override
    public PersonDto getPersonByUserId(Long id) {
        return modelMapper
                .map(personRepository.findById(id)
                        .orElseThrow(PersonNotFoundException::new), PersonDto.class);
    }

    @Override
    public void updatePersonByUserId(Long id, PersonDto updatedPerson) {
        User user = getCurrentUser();
        updatedPerson.setUser(user);
        updatedPerson.setUserId(id);
        personRepository.save(modelMapper.map(updatedPerson, Person.class));
    }

    @Override
    public User getCurrentUser() {
        return userRepository
                .findByPhone(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
