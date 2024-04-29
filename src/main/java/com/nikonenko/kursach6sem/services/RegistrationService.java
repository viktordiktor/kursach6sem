package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.requests.RegisterDto;
import org.springframework.validation.BindingResult;

public interface RegistrationService {
    String register(RegisterDto registerDto, BindingResult bindingResult);
}
