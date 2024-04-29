package com.nikonenko.kursach6sem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/general")
public class GeneralController {

    @GetMapping("/about")
    public String getAboutPage() {
        return "general/about";
    }
}
