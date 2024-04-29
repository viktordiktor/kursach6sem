package com.nikonenko.kursach6sem.repositories;

import com.nikonenko.kursach6sem.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
