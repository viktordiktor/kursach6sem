package com.nikonenko.kursach6sem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecreationCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecreationCenterApplication.class, args);
    }
}
