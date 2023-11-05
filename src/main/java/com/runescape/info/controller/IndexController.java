package com.runescape.info.controller;

import com.runescape.info.service.ClanmembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final ClanmembersService clanmembersService;

    @Autowired
    public IndexController(final ClanmembersService clanmembersService) {
        this.clanmembersService = clanmembersService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("clanmembers", clanmembersService.getAllClanmembers());
        return "index";
    }
}
