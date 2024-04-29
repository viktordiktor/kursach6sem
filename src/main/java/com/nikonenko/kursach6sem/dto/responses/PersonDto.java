package com.nikonenko.kursach6sem.dto.responses;

import com.nikonenko.kursach6sem.models.User;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long userId;
    private User user;
    @Size(min = 3, max = 40, message = "Количество символов должно быть от 3 до 40")
    private String name;
    private String gender;
}
