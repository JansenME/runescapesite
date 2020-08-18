package com.runescape.info.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OwnErrorController implements ErrorController {
    @Override
    @GetMapping("/error")
    public String getErrorPath() {
        return "/error";
    }
}
