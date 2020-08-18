package com.runescape.info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CalculatorsController {
    @GetMapping("/calculators")
    public String getCalculators() {
        return "calculators";
    }

    @PostMapping("/calculators")
    public String goToCalculators() {
        return "calculators";
    }
}
