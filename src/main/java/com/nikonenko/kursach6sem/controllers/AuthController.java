package com.nikonenko.kursach6sem.controllers;

import com.nikonenko.kursach6sem.dto.requests.RegisterDto;
import com.nikonenko.kursach6sem.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("register_request") RegisterDto registerDto) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("register_request") @Valid RegisterDto registerDto,
                                      BindingResult bindingResult) {
        return registrationService.register(registerDto, bindingResult);
    }
}
