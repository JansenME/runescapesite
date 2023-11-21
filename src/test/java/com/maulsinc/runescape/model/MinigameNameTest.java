package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinigameNameTest {

    @Test
    void testGetMinigameEnumByNumberHappyFlow() {
        assertEquals(MinigameName.BOUNTY_HUNTER, MinigameName.getMinigameNameByNumber(0));
        assertEquals(MinigameName.BH_ROGUES, MinigameName.getMinigameNameByNumber(1));
        assertEquals(MinigameName.DOMINION_TOWER, MinigameName.getMinigameNameByNumber(2));
        assertEquals(MinigameName.THE_CRUCIBLE, MinigameName.getMinigameNameByNumber(3));
        assertEquals(MinigameName.CASTLE_WARS_GAMES, MinigameName.getMinigameNameByNumber(4));
        assertEquals(MinigameName.BA_ATTACKERS, MinigameName.getMinigameNameByNumber(5));
        assertEquals(MinigameName.BA_DEFENDERS, MinigameName.getMinigameNameByNumber(6));
        assertEquals(MinigameName.BA_COLLECTORS, MinigameName.getMinigameNameByNumber(7));
        assertEquals(MinigameName.BA_HEALERS, MinigameName.getMinigameNameByNumber(8));
        assertEquals(MinigameName.DUEL_TOURNAMENT, MinigameName.getMinigameNameByNumber(9));
        assertEquals(MinigameName.MOBILISING_ARMIES, MinigameName.getMinigameNameByNumber(10));
        assertEquals(MinigameName.CONQUEST, MinigameName.getMinigameNameByNumber(11));
        assertEquals(MinigameName.FIST_OF_GUTHIX, MinigameName.getMinigameNameByNumber(12));
        assertEquals(MinigameName.GG_ATHLETICS, MinigameName.getMinigameNameByNumber(13));
        assertEquals(MinigameName.GG_RESOURCE_RACE, MinigameName.getMinigameNameByNumber(14));
        assertEquals(MinigameName.WE2_ARMADYL_LIFETIME_CONTRIBUTION, MinigameName.getMinigameNameByNumber(15));
        assertEquals(MinigameName.WE2_BANDOS_LIFETIME_CONTRIBUTION, MinigameName.getMinigameNameByNumber(16));
        assertEquals(MinigameName.WE2_ARMADYL_PVP_KILLS, MinigameName.getMinigameNameByNumber(17));
        assertEquals(MinigameName.WE2_BANDOS_PVP_KILLS, MinigameName.getMinigameNameByNumber(18));
        assertEquals(MinigameName.HEIST_GUARD_LEVEL, MinigameName.getMinigameNameByNumber(19));
        assertEquals(MinigameName.HEIST_ROBBER_LEVEL, MinigameName.getMinigameNameByNumber(20));
        assertEquals(MinigameName.CFP_5_GAME_AVERAGE, MinigameName.getMinigameNameByNumber(21));
        assertEquals(MinigameName.AF15_COW_TIPPING, MinigameName.getMinigameNameByNumber(22));
        assertEquals(MinigameName.AF15_RATS_KILLED_AFTER_THE_MINIQUEST, MinigameName.getMinigameNameByNumber(23));
        assertEquals(MinigameName.RUNESCORE, MinigameName.getMinigameNameByNumber(24));
        assertEquals(MinigameName.CLUE_SCROLLS_EASY, MinigameName.getMinigameNameByNumber(25));
        assertEquals(MinigameName.CLUE_SCROLLS_MEDIUM, MinigameName.getMinigameNameByNumber(26));
        assertEquals(MinigameName.CLUE_SCROLLS_HARD, MinigameName.getMinigameNameByNumber(27));
        assertEquals(MinigameName.CLUE_SCROLLS_ELITE, MinigameName.getMinigameNameByNumber(28));
        assertEquals(MinigameName.CLUE_SCROLLS_MASTER, MinigameName.getMinigameNameByNumber(29));
    }

    @Test
    void testGetMinigameEnumByNumberUnhappyFlow() {
        assertNull(MinigameName.getMinigameNameByNumber(-1));
        assertNull(MinigameName.getMinigameNameByNumber(30));
    }
}