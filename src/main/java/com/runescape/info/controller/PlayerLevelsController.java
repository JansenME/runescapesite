package com.runescape.info.controller;

import com.runescape.info.entity.PlayerLevels;
import com.runescape.info.service.PlayerLevelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerLevelsController {
    private final PlayerLevelsService playerLevelsService;

    @Autowired
    public PlayerLevelsController(final PlayerLevelsService playerLevelsService) {
        this.playerLevelsService = playerLevelsService;
    }

    @GetMapping("/playerlevels/get/{player}")
    public List<PlayerLevels> getPlayerLevelsFromDatabase(@PathVariable("player") String player) {
        return playerLevelsService.getPlayerLevelsFromDatabase(player);
    }

    @GetMapping("/playerlevels/get-newest/{player}")
    public PlayerLevels getNewestPlayerLevelsFromDatabase(@PathVariable("player") String player) {
        return playerLevelsService.getNewestPlayerLevelsFromDatabase(player);
    }

    @PostMapping("/playerlevels/save/{player}")
    public void savePlayerLevels(@PathVariable("player") String player) {
        playerLevelsService.savePlayerLevelsToDatabase(player);
    }
}
