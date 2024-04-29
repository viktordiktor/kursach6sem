package com.nikonenko.kursach6sem.controllers;

import com.nikonenko.kursach6sem.dto.ReviewDto;
import com.nikonenko.kursach6sem.dto.responses.PersonDto;
import com.nikonenko.kursach6sem.dto.responses.UserDto;
import com.nikonenko.kursach6sem.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        userService.addAdminDashboardAttributes(model);
        return "admin/dashboard";
    }

    @GetMapping("/admin/bookings")
    public String adminBookings(Model model) {
        userService.addAdminBookingsAttribute(model);
        return "admin/bookings";
    }

    @GetMapping("/admin/recreation-objects")
    public String adminRecreationObjects(Model model) {
        userService.addAdminRecreationObjectsAttribute(model);
        return "admin/recreation-objects";
    }

    @GetMapping("/profile")
    public String profile(Model model, @ModelAttribute ReviewDto reviewDTO) {
        userService.addProfileAttributes(model);
        return "users/profile";
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute @Valid UserDto updatedUser,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/profile";
        userService.updateUser(id, updatedUser);
        return "redirect:/api/v1/users/profile";
    }

    @PatchMapping("/{id}/person")
    public String updateUserPerson(@PathVariable Long id, @ModelAttribute @Valid PersonDto updatedPerson,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/profile";
        userService.updatePersonByUserId(id, updatedPerson);
        return "redirect:/api/v1/users/profile";
    }
}
