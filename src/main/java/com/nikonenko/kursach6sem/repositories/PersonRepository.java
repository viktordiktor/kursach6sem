package com.nikonenko.kursach6sem.repositories;

import com.nikonenko.kursach6sem.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
