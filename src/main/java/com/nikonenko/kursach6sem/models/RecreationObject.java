package com.nikonenko.kursach6sem.models;

import com.nikonenko.kursach6sem.dto.responses.UserDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "recreation_object")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RecreationObject {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "name")
    private String name;

    @Column(name = "available_guests")
    private int availableGuests;

    @Column(name = "description")
    private String description;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "main_photo")
    private String mainPhoto;

    @OneToMany(mappedBy = "recreationObject", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "recreationObject", cascade = CascadeType.ALL)
    private Set<Photo> photos;

    @OneToMany(mappedBy = "recreationObject", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    public boolean hasReviewForUser(UserDto userDto) {
        return reviews.stream().anyMatch(review -> review.getUser().getId().equals(userDto.getId()));
    }
}
