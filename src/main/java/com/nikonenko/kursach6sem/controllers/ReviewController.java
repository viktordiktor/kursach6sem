package com.nikonenko.kursach6sem.controllers;

import com.nikonenko.kursach6sem.dto.ReviewDto;
import com.nikonenko.kursach6sem.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;


    @PostMapping("/{objectId}")
    public String createReview(@RequestBody ReviewDto reviewDto, @PathVariable Long objectId) {
        reviewService.createReview(reviewDto, objectId);
        return "redirect:/api/v1/users/profile";
    }
}
