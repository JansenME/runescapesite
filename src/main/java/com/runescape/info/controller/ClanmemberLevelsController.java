package com.runescape.info.controller;

import com.runescape.info.service.ClanmemberLevelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClanmemberLevelsController {
    @Value("${app.version:unknown}")
    String version;

    private final ClanmemberLevelsService clanmemberLevelsService;

    @Autowired
    public ClanmemberLevelsController(final ClanmemberLevelsService clanmemberLevelsService) {
        this.clanmemberLevelsService = clanmemberLevelsService;
    }

    @GetMapping("/clanmember/{name}")
    public String getClanMemberLevels(Model model, @PathVariable String name) {
        model.addAttribute("versionNumber", version);
        model.addAttribute("clanmemberLevels", clanmemberLevelsService.getOneClanmemberLevels(name));
        model.addAttribute("clanmemberName", name);
        return "clanmember";
    }
}
