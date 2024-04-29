package com.nikonenko.kursach6sem.repositories;

import com.nikonenko.kursach6sem.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByRecreationObjectId(Long id);
}
