package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.ReviewDto;
import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.models.Review;
import com.nikonenko.kursach6sem.models.User;
import com.nikonenko.kursach6sem.repositories.ReviewRepository;
import com.nikonenko.kursach6sem.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private RecreationObjectService recreationObjectService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void testCreateReview() {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setRating(5);
        reviewDto.setComment("Good");

        User user = new User();
        RecreationObject recreationObject = new RecreationObject();
        RecreationObjectDto recreationObjectDto = new RecreationObjectDto();

        doReturn(user).when(userService).getCurrentUser();
        doReturn(recreationObjectDto).when(recreationObjectService).getRecreationObjectById(anyLong());
        doReturn(recreationObject).when(modelMapper).map(any(RecreationObjectDto.class), eq(RecreationObject.class));

        reviewService.createReview(reviewDto, 1L);

        verify(reviewRepository).save(any(Review.class));
    }
}