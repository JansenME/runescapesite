package com.runescape.info.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinigameEnumTest {

    @Test
    void testGetNameForImageHappyFlow() {
        assertEquals("bountyhunter", MinigameEnum.BOUNTY_HUNTER.getNameForImage());
        assertEquals("bhrogues", MinigameEnum.BH_ROGUES.getNameForImage());
        assertEquals("dominiontower", MinigameEnum.DOMINION_TOWER.getNameForImage());
        assertEquals("thecrucible", MinigameEnum.THE_CRUCIBLE.getNameForImage());
        assertEquals("castlewarsgames", MinigameEnum.CASTLE_WARS_GAMES.getNameForImage());
        assertEquals("baattackers", MinigameEnum.BA_ATTACKERS.getNameForImage());
        assertEquals("badefenders", MinigameEnum.BA_DEFENDERS.getNameForImage());
        assertEquals("bacollectors", MinigameEnum.BA_COLLECTORS.getNameForImage());
        assertEquals("bahealers", MinigameEnum.BA_HEALERS.getNameForImage());
        assertEquals("dueltournament", MinigameEnum.DUEL_TOURNAMENT.getNameForImage());
        assertEquals("mobilisingarmies", MinigameEnum.MOBILISING_ARMIES.getNameForImage());
        assertEquals("conquest", MinigameEnum.CONQUEST.getNameForImage());
        assertEquals("fistofguthix", MinigameEnum.FIST_OF_GUTHIX.getNameForImage());
        assertEquals("ggathletics", MinigameEnum.GG_ATHLETICS.getNameForImage());
        assertEquals("ggresourcerace", MinigameEnum.GG_RESOURCE_RACE.getNameForImage());
        assertEquals("we2armadyllifetimecontribution", MinigameEnum.WE2_ARMADYL_LIFETIME_CONTRIBUTION.getNameForImage());
        assertEquals("we2bandoslifetimecontribution", MinigameEnum.WE2_BANDOS_LIFETIME_CONTRIBUTION.getNameForImage());
        assertEquals("we2armadylpvpkills", MinigameEnum.WE2_ARMADYL_PVP_KILLS.getNameForImage());
        assertEquals("we2bandospvpkills", MinigameEnum.WE2_BANDOS_PVP_KILLS.getNameForImage());
        assertEquals("heistguardlevel", MinigameEnum.HEIST_GUARD_LEVEL.getNameForImage());
        assertEquals("heistrobberlevel", MinigameEnum.HEIST_ROBBER_LEVEL.getNameForImage());
        assertEquals("cfp5gameaverage", MinigameEnum.CFP_5_GAME_AVERAGE.getNameForImage());
        assertEquals("af15cowtipping", MinigameEnum.AF15_COW_TIPPING.getNameForImage());
        assertEquals("af15ratskilledaftertheminiquest", MinigameEnum.AF15_RATS_KILLED_AFTER_THE_MINIQUEST.getNameForImage());
        assertEquals("runescore", MinigameEnum.RUNESCORE.getNameForImage());
        assertEquals("cluescrollseasy", MinigameEnum.CLUE_SCROLLS_EASY.getNameForImage());
        assertEquals("cluescrollsmedium", MinigameEnum.CLUE_SCROLLS_MEDIUM.getNameForImage());
        assertEquals("cluescrollshard", MinigameEnum.CLUE_SCROLLS_HARD.getNameForImage());
        assertEquals("cluescrollselite", MinigameEnum.CLUE_SCROLLS_ELITE.getNameForImage());
        assertEquals("cluescrollsmaster", MinigameEnum.CLUE_SCROLLS_MASTER.getNameForImage());
    }

    @Test
    void testGetMinigameEnumByNumberHappyFlow() {
        assertEquals(MinigameEnum.BOUNTY_HUNTER, MinigameEnum.getMinigameEnumByNumber(0));
        assertEquals(MinigameEnum.BH_ROGUES, MinigameEnum.getMinigameEnumByNumber(1));
        assertEquals(MinigameEnum.DOMINION_TOWER, MinigameEnum.getMinigameEnumByNumber(2));
        assertEquals(MinigameEnum.THE_CRUCIBLE, MinigameEnum.getMinigameEnumByNumber(3));
        assertEquals(MinigameEnum.CASTLE_WARS_GAMES, MinigameEnum.getMinigameEnumByNumber(4));
        assertEquals(MinigameEnum.BA_ATTACKERS, MinigameEnum.getMinigameEnumByNumber(5));
        assertEquals(MinigameEnum.BA_DEFENDERS, MinigameEnum.getMinigameEnumByNumber(6));
        assertEquals(MinigameEnum.BA_COLLECTORS, MinigameEnum.getMinigameEnumByNumber(7));
        assertEquals(MinigameEnum.BA_HEALERS, MinigameEnum.getMinigameEnumByNumber(8));
        assertEquals(MinigameEnum.DUEL_TOURNAMENT, MinigameEnum.getMinigameEnumByNumber(9));
        assertEquals(MinigameEnum.MOBILISING_ARMIES, MinigameEnum.getMinigameEnumByNumber(10));
        assertEquals(MinigameEnum.CONQUEST, MinigameEnum.getMinigameEnumByNumber(11));
        assertEquals(MinigameEnum.FIST_OF_GUTHIX, MinigameEnum.getMinigameEnumByNumber(12));
        assertEquals(MinigameEnum.GG_ATHLETICS, MinigameEnum.getMinigameEnumByNumber(13));
        assertEquals(MinigameEnum.GG_RESOURCE_RACE, MinigameEnum.getMinigameEnumByNumber(14));
        assertEquals(MinigameEnum.WE2_ARMADYL_LIFETIME_CONTRIBUTION, MinigameEnum.getMinigameEnumByNumber(15));
        assertEquals(MinigameEnum.WE2_BANDOS_LIFETIME_CONTRIBUTION, MinigameEnum.getMinigameEnumByNumber(16));
        assertEquals(MinigameEnum.WE2_ARMADYL_PVP_KILLS, MinigameEnum.getMinigameEnumByNumber(17));
        assertEquals(MinigameEnum.WE2_BANDOS_PVP_KILLS, MinigameEnum.getMinigameEnumByNumber(18));
        assertEquals(MinigameEnum.HEIST_GUARD_LEVEL, MinigameEnum.getMinigameEnumByNumber(19));
        assertEquals(MinigameEnum.HEIST_ROBBER_LEVEL, MinigameEnum.getMinigameEnumByNumber(20));
        assertEquals(MinigameEnum.CFP_5_GAME_AVERAGE, MinigameEnum.getMinigameEnumByNumber(21));
        assertEquals(MinigameEnum.AF15_COW_TIPPING, MinigameEnum.getMinigameEnumByNumber(22));
        assertEquals(MinigameEnum.AF15_RATS_KILLED_AFTER_THE_MINIQUEST, MinigameEnum.getMinigameEnumByNumber(23));
        assertEquals(MinigameEnum.RUNESCORE, MinigameEnum.getMinigameEnumByNumber(24));
        assertEquals(MinigameEnum.CLUE_SCROLLS_EASY, MinigameEnum.getMinigameEnumByNumber(25));
        assertEquals(MinigameEnum.CLUE_SCROLLS_MEDIUM, MinigameEnum.getMinigameEnumByNumber(26));
        assertEquals(MinigameEnum.CLUE_SCROLLS_HARD, MinigameEnum.getMinigameEnumByNumber(27));
        assertEquals(MinigameEnum.CLUE_SCROLLS_ELITE, MinigameEnum.getMinigameEnumByNumber(28));
        assertEquals(MinigameEnum.CLUE_SCROLLS_MASTER, MinigameEnum.getMinigameEnumByNumber(29));
    }

    @Test
    void testGetMinigameEnumByNumberUnhappyFlow() {
        assertNull(MinigameEnum.getMinigameEnumByNumber(-1));
        assertNull(MinigameEnum.getMinigameEnumByNumber(30));
    }
}