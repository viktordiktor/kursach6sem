package com.nikonenko.kursach6sem.repositories;

import com.nikonenko.kursach6sem.models.RecreationObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecreationObjectRepository extends JpaRepository<RecreationObject, Long> {
    @Query("SELECT o FROM RecreationObject o WHERE o.objectType LIKE %:search% OR o.name LIKE %:search% OR o.description LIKE %:search%")
    Page<RecreationObject> findBySearch(@Param("search") String search, Pageable pageable);
}
