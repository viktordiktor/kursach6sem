package com.nikonenko.kursach6sem.services.impl;

import com.nikonenko.kursach6sem.dto.requests.RegisterDto;
import com.nikonenko.kursach6sem.models.Person;
import com.nikonenko.kursach6sem.models.User;
import com.nikonenko.kursach6sem.repositories.PersonRepository;
import com.nikonenko.kursach6sem.repositories.UserRepository;
import com.nikonenko.kursach6sem.services.RegistrationService;
import com.nikonenko.kursach6sem.utils.UserValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Transactional
    public String register(RegisterDto registerDto, BindingResult bindingResult) {
        userValidator.validate(registerDto, bindingResult);

        if (bindingResult.hasErrors())
            return "auth/registration";

        User user = userRepository.save(User.builder()
                .phone(registerDto.getPhone())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role("ROLE_USER")
                .build());

        Person person = modelMapper.map(registerDto, Person.class);
        person.setUser(user);
        personRepository.save(person);

        return "redirect:/api/v1/auth/login";
    }
}
