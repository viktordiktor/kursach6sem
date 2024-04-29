package com.nikonenko.kursach6sem.utils;

import com.nikonenko.kursach6sem.dto.requests.RegisterDto;
import com.nikonenko.kursach6sem.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserDetailsService userDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDto authRequest = (RegisterDto) target;
        try {
            userDetailsService.loadUserByUsername(authRequest.getPhone());
        } catch(UsernameNotFoundException e){
            return;
        }
        errors.rejectValue("phone", "", "User already exists");
    }
}
