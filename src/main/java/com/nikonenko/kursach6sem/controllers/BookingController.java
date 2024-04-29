package com.nikonenko.kursach6sem.controllers;

import com.nikonenko.kursach6sem.dto.requests.BookingsRequest;
import com.nikonenko.kursach6sem.services.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{objectId}")
    public String getBookingPage(@PathVariable Long objectId, Model model) {
        model.addAttribute("bookings", bookingService.getBookingsByObjectId(objectId));
        model.addAttribute("id", objectId);
        return "bookings/new";
    }

    @PostMapping("/{objectId}")
    public String createBooking(@RequestBody BookingsRequest request, @PathVariable Long objectId) {
        bookingService.createBooking(request, objectId);
        return "redirect:/api/v1/users/profile";
    }

    @DeleteMapping("/{id}")
    public String closeBooking(@PathVariable Long id, HttpServletRequest request) {
        bookingService.closeBooking(id);
        return request.getHeader("referer").contains("profile")
                ? "redirect:/api/v1/users/profile"
                : "redirect:/api/v1/users/admin/bookings";
    }
}
