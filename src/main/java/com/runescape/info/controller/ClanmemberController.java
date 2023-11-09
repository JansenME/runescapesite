package com.runescape.info.controller;

import com.runescape.info.service.ClanmemberLevelsService;
import com.runescape.info.service.ClanmemberMinigamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClanmemberController {
    @Value("${app.version:unknown}")
    String version;

    private final ClanmemberLevelsService clanmemberLevelsService;
    private final ClanmemberMinigamesService clanmemberMinigamesService;

    @Autowired
    public ClanmemberController(final ClanmemberLevelsService clanmemberLevelsService, final ClanmemberMinigamesService clanmemberMinigamesService) {
        this.clanmemberLevelsService = clanmemberLevelsService;
        this.clanmemberMinigamesService = clanmemberMinigamesService;
    }

    @GetMapping("/clanmember/{name}")
    public String getClanMemberLevels(Model model, @PathVariable String name) {
        model.addAttribute("versionNumber", version);
        model.addAttribute("clanmemberLevels", clanmemberLevelsService.getOneClanmemberLevels(name));
        model.addAttribute("clanmemberMinigames", clanmemberMinigamesService.getOneClanmemberMinigames(name));
        model.addAttribute("clanmemberName", name);
        return "clanmember";
    }
}
