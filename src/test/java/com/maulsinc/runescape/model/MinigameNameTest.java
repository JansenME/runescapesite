package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinigameNameTest {

    @Test
    void testGetNameForImageHappyFlow() {
        assertEquals("bountyhunter", MinigameName.BOUNTY_HUNTER.getNameForImage());
        assertEquals("bhrogues", MinigameName.BH_ROGUES.getNameForImage());
        assertEquals("dominiontower", MinigameName.DOMINION_TOWER.getNameForImage());
        assertEquals("thecrucible", MinigameName.THE_CRUCIBLE.getNameForImage());
        assertEquals("castlewarsgames", MinigameName.CASTLE_WARS_GAMES.getNameForImage());
        assertEquals("baattackers", MinigameName.BA_ATTACKERS.getNameForImage());
        assertEquals("badefenders", MinigameName.BA_DEFENDERS.getNameForImage());
        assertEquals("bacollectors", MinigameName.BA_COLLECTORS.getNameForImage());
        assertEquals("bahealers", MinigameName.BA_HEALERS.getNameForImage());
        assertEquals("dueltournament", MinigameName.DUEL_TOURNAMENT.getNameForImage());
        assertEquals("mobilisingarmies", MinigameName.MOBILISING_ARMIES.getNameForImage());
        assertEquals("conquest", MinigameName.CONQUEST.getNameForImage());
        assertEquals("fistofguthix", MinigameName.FIST_OF_GUTHIX.getNameForImage());
        assertEquals("ggathletics", MinigameName.GG_ATHLETICS.getNameForImage());
        assertEquals("ggresourcerace", MinigameName.GG_RESOURCE_RACE.getNameForImage());
        assertEquals("we2armadyllifetimecontribution", MinigameName.WE2_ARMADYL_LIFETIME_CONTRIBUTION.getNameForImage());
        assertEquals("we2bandoslifetimecontribution", MinigameName.WE2_BANDOS_LIFETIME_CONTRIBUTION.getNameForImage());
        assertEquals("we2armadylpvpkills", MinigameName.WE2_ARMADYL_PVP_KILLS.getNameForImage());
        assertEquals("we2bandospvpkills", MinigameName.WE2_BANDOS_PVP_KILLS.getNameForImage());
        assertEquals("heistguardlevel", MinigameName.HEIST_GUARD_LEVEL.getNameForImage());
        assertEquals("heistrobberlevel", MinigameName.HEIST_ROBBER_LEVEL.getNameForImage());
        assertEquals("cfp5gameaverage", MinigameName.CFP_5_GAME_AVERAGE.getNameForImage());
        assertEquals("af15cowtipping", MinigameName.AF15_COW_TIPPING.getNameForImage());
        assertEquals("af15ratskilledaftertheminiquest", MinigameName.AF15_RATS_KILLED_AFTER_THE_MINIQUEST.getNameForImage());
        assertEquals("runescore", MinigameName.RUNESCORE.getNameForImage());
        assertEquals("cluescrollseasy", MinigameName.CLUE_SCROLLS_EASY.getNameForImage());
        assertEquals("cluescrollsmedium", MinigameName.CLUE_SCROLLS_MEDIUM.getNameForImage());
        assertEquals("cluescrollshard", MinigameName.CLUE_SCROLLS_HARD.getNameForImage());
        assertEquals("cluescrollselite", MinigameName.CLUE_SCROLLS_ELITE.getNameForImage());
        assertEquals("cluescrollsmaster", MinigameName.CLUE_SCROLLS_MASTER.getNameForImage());
    }

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