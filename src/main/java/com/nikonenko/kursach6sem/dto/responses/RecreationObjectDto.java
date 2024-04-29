package com.nikonenko.kursach6sem.dto.responses;

import com.nikonenko.kursach6sem.models.Booking;
import com.nikonenko.kursach6sem.models.Photo;
import com.nikonenko.kursach6sem.models.Review;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecreationObjectDto {
    private Long id;
    @Size(min = 3, max = 20, message = "Количество символов должно быть от 3 до 20")
    private String objectType;
    @Size(min = 3, max = 40, message = "Количество символов должно быть от 3 до 40")
    private String name;
    @Min(value = 1)
    private int availableGuests;
    private String description;
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerDay;
    private String mainPhoto;
    private Set<Review> reviews;
    private Set<Photo> photos;
    private Set<Booking> bookings;
    private Double rating;
}
