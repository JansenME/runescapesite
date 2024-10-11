package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinigameTest {

    @Test
    void testGetFormattedRank() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, 50000L, null).getFormattedRank();

        assertEquals("50.000", result);
    }

    @Test
    void testGetFormattedRankMinusOneValue() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, -1L, null).getFormattedRank();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedRankNullValue() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, -1L, null).getFormattedRank();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedScore() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, null, 50000L).getFormattedScore();

        assertEquals("50.000", result);
    }

    @Test
    void testGetFormattedScoreMinusOneValue() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, null, -1L).getFormattedScore();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedScoreNullValue() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, null, -1L).getFormattedScore();

        assertEquals("--", result);
    }

    @Test
    void testGetMinigameNameStringHappyFlow() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, 50415L, 5585478L).getMinigameNameString();

        assertEquals("AF15: Cow Tipping", result);
    }

    @Test
    void testGetMinigameNameStringMinigameNameNull() {
        String result = createMinigame(null, 50415L, 5585478L).getMinigameNameString();

        assertEquals("", result);
    }

    @Test
    void testGetMinigameNameForImageHappyFlow() {
        String result = createMinigame(MinigameName.AF15_COW_TIPPING, 50415L, 5585478L).getMinigameNameForImage();

        assertEquals("af15cowtipping", result);
    }

    @Test
    void testGetMinigameNameForImageMinigameNameNull() {
        String result = createMinigame(null, 50415L, 5585478L).getMinigameNameForImage();

        assertEquals("", result);
    }

    private Minigame createMinigame(final MinigameName minigameName, final Long rank, final Long score) {
        Minigame minigame = new Minigame();
        minigame.setMinigameName(minigameName);
        minigame.setRank(rank);
        minigame.setScore(score);

        return minigame;
    }
}