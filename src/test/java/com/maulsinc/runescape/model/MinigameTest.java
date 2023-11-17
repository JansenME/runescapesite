package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinigameTest {

    @Test
    void testGetFormattedRank() {
        String result = createMinigame(50000L, null).getFormattedRank();

        assertEquals("50.000", result);
    }

    @Test
    void testGetFormattedRankMinusOneValue() {
        String result = createMinigame(-1L, null).getFormattedRank();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedRankNullValue() {
        String result = createMinigame(-1L, null).getFormattedRank();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedScore() {
        String result = createMinigame(null, 50000L).getFormattedScore();

        assertEquals("50.000", result);
    }

    @Test
    void testGetFormattedScoreMinusOneValue() {
        String result = createMinigame(null, -1L).getFormattedScore();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedScoreNullValue() {
        String result = createMinigame(null, -1L).getFormattedScore();

        assertEquals("--", result);
    }

    private Minigame createMinigame(final Long rank, final Long score) {
        Minigame minigame = new Minigame();
        minigame.setMinigameName(MinigameName.AF15_COW_TIPPING);
        minigame.setRank(rank);
        minigame.setScore(score);

        return minigame;
    }
}