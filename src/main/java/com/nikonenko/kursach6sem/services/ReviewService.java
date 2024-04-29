package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.ReviewDto;

public interface ReviewService {
    void createReview(ReviewDto reviewDto, Long objectId);
}
