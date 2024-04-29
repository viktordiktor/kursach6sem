package com.nikonenko.kursach6sem.services.impl;

import com.nikonenko.kursach6sem.dto.ReviewDto;
import com.nikonenko.kursach6sem.exceptions.RecreationObjectNotFoundException;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.models.Review;
import com.nikonenko.kursach6sem.repositories.RecreationObjectRepository;
import com.nikonenko.kursach6sem.repositories.ReviewRepository;
import com.nikonenko.kursach6sem.services.RecreationObjectService;
import com.nikonenko.kursach6sem.services.ReviewService;
import com.nikonenko.kursach6sem.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final RecreationObjectService recreationObjectService;
    private final ModelMapper modelMapper;

    @Override
    public void createReview(ReviewDto reviewDto, Long objectId) {
        reviewRepository.save(Review.builder()
                .user(userService.getCurrentUser())
                .recreationObject(modelMapper
                        .map(recreationObjectService.getRecreationObjectById(objectId), RecreationObject.class))
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .build());
    }
}
