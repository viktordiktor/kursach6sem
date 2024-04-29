package com.nikonenko.kursach6sem.dto.requests;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    @Pattern(regexp = "\\+375\\d{9}")
    private String phone;
    @Size(min = 3, max = 20, message = "Количество символов должно быть от 3 до 20")
    private String password;
}
