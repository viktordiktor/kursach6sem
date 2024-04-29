package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.responses.PersonDto;
import com.nikonenko.kursach6sem.dto.responses.UserDto;
import com.nikonenko.kursach6sem.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface UserService extends UserDetailsService {
    List<UserDto> getAllUsers();
    void addAdminDashboardAttributes(Model model);
    void addAdminBookingsAttribute(Model model);
    void addAdminRecreationObjectsAttribute(Model model);
    void addProfileAttributes(Model model);
    User getCurrentUser();
    void updateUser(Long id, UserDto updatedUser);
    PersonDto getPersonByUserId(Long id);
    void updatePersonByUserId(Long id, PersonDto updatedPerson);
}
