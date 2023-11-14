package com.maulsinc.runescape.controller;

import com.maulsinc.runescape.service.ClanmembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Value("${app.version:unknown}")
    String version;

    private final ClanmembersService clanmembersService;

    @Autowired
    public IndexController(final ClanmembersService clanmembersService) {
        this.clanmembersService = clanmembersService;
    }

    @GetMapping(value={"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("versionNumber", version);
        model.addAttribute("clanmembers", clanmembersService.getAllClanmembers());
        return "index";
    }
}
