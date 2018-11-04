package com.runescape.info.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class MembersController {
    @GetMapping("/members")
    public String goToMembers() {
        return "members";
    }

    @PostMapping("/member")
    public String sendMemberName() {
        return "member";
    }

    @GetMapping("/member/{name}")
    public String getMembersInfo(@PathVariable("name") String name) {
        log.info(name);
        return "member";
    }
}
