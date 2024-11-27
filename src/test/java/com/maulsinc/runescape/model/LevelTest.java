package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelTest {
    @Test
    void testGetFormattedRank() {
        String result = createLevelWithCorrectValues().getFormattedRank();

        assertEquals("5.000", result);
    }

    @Test
    void testGetFormattedRankMinusOneValue() {
        String result = createLevelWithExperienceMinusOne().getFormattedRank();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedRankIronman() {
        String result = createLevelWithCorrectValues().getFormattedRankIronman();

        assertEquals("45.000", result);
    }

    @Test
    void testGetFormattedRankIronmanMinusOneValue() {
        String result = createLevelWithExperienceMinusOne().getFormattedRankIronman();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedRankHardcoreIronman() {
        String result = createLevelWithCorrectValues().getFormattedRankHardcoreIronman();

        assertEquals("46.000", result);
    }

    @Test
    void testGetFormattedRankHardcoreIronmanMinusOneValue() {
        String result = createLevelWithExperienceMinusOne().getFormattedRankHardcoreIronman();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedLevel() {
        String result = createLevelWithCorrectValues().getFormattedLevel();

        assertEquals("150", result);
    }

    @Test
    void testGetFormattedLevelMinusOneValue() {
        String result = createLevelWithExperienceMinusOne().getFormattedLevel();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedExperience() {
        String result = createLevelWithCorrectValues().getFormattedExperience();

        assertEquals("2.500.000", result);
    }

    @Test
    void testGetFormattedExperienceMinusOneValue() {
        String result = createLevelWithExperienceMinusOne().getFormattedExperience();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedExperienceToday() {
        String result = createLevelWithCorrectValues().getFormattedExperienceToday();

        assertEquals("250.000", result);
    }

    @Test
    void testGetFormattedExperienceTodayNullValue() {
        Level level = new Level();
        level.setExperienceToday(null);

        String result = level.getFormattedExperienceToday();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedExperienceTodayMinusOneValue() {
        Level level = new Level();
        level.setExperienceToday(-1L);

        String result = level.getFormattedExperienceToday();

        assertEquals("--", result);
    }

    @Test
    void testGetFormattedExperienceTodayZeroValue() {
        Level level = new Level();
        level.setExperienceToday(0L);

        String result = level.getFormattedExperienceToday();

        assertEquals("--", result);
    }

    private Level createLevelWithCorrectValues() {
        Level level = new Level();
        level.setSkill(Skill.AGILITY);
        level.setRank(5000L);
        level.setRankIronman(45000L);
        level.setRankHardcoreIronman(46000L);
        level.setLevel(150L);
        level.setExperience(2500000L);
        level.setExperienceToday(250000L);

        return level;
    }

    private Level createLevelWithExperienceMinusOne() {
        Level level = new Level();
        level.setExperience(-1L);

        return level;
    }
}