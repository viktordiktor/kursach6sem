package com.nikonenko.kursach6sem.repositories;

import com.nikonenko.kursach6sem.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
