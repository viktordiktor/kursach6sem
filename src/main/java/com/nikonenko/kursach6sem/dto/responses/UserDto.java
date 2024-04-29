package com.nikonenko.kursach6sem.dto.responses;
import com.nikonenko.kursach6sem.models.Booking;
import com.nikonenko.kursach6sem.models.Person;
import com.nikonenko.kursach6sem.models.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String phone;
    private String password;
    private String role;
    private Person person;
    private Set<Review> reviews;
    private Set<Booking> bookings;
}
