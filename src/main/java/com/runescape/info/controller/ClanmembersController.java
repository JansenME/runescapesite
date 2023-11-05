package com.runescape.info.controller;

import com.runescape.info.model.Clanmember;
import com.runescape.info.model.ClanmemberLevels;
import com.runescape.info.service.ClanmemberLevelsService;
import com.runescape.info.service.ClanmembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClanmembersController {
    private final ClanmembersService clanmembersService;
    private final ClanmemberLevelsService clanmemberLevelsService;

    @Autowired
    public ClanmembersController(final ClanmembersService clanmembersService, final ClanmemberLevelsService clanmemberLevelsService) {
        this.clanmembersService = clanmembersService;
        this.clanmemberLevelsService = clanmemberLevelsService;
    }

    @GetMapping("/clanmembers/all")
    public List<Clanmember> getAllClanmembers() {
        return clanmembersService.getAllClanmembers();
    }

    @GetMapping("/clanmember/levels/{name}")
    public ClanmemberLevels getClanMemberLevels(@PathVariable String name) {
        return clanmemberLevelsService.getOneClanmemberLevels(name);
    }
}
