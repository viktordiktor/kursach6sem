package com.nikonenko.kursach6sem.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({RecreationObjectNotFoundException.class, BookingNotFoundException.class,
            PersonNotFoundException.class})
    public String handleNotFoundException(Exception ex) {
        return "/error/404";
    }
}
