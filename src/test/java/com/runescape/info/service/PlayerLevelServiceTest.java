package com.runescape.info.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PlayerLevelServiceTest {
    private static HiScoresService hiScoresService;
    private static PlayerLevelsService playerLevelsService;

    @BeforeAll
    public static void setUp() {
        hiScoresService = new HiScoresService();
        playerLevelsService = new PlayerLevelsService(hiScoresService);
    }

    @Test
    void getPlayerLevels() {
        playerLevelsService.getPlayerLevels("HC_Kloeperd");
    }
}