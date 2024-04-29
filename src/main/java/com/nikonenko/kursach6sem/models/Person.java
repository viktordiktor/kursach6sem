package com.nikonenko.kursach6sem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Person {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(optional = true)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name")
    private String name;

    @Column(name = "gender")
    private String gender;
}
