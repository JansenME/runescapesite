package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QuestDifficultyTest {

    @Test
    void testGetQuestDifficultyByNumberHappyFlow() {
        assertEquals(QuestDifficulty.NOVICE, QuestDifficulty.getQuestDifficultyByNumber(0));
        assertEquals(QuestDifficulty.INTERMEDIATE, QuestDifficulty.getQuestDifficultyByNumber(1));
        assertEquals(QuestDifficulty.EXPERIENCED, QuestDifficulty.getQuestDifficultyByNumber(2));
        assertEquals(QuestDifficulty.MASTER, QuestDifficulty.getQuestDifficultyByNumber(3));
        assertEquals(QuestDifficulty.GRANDMASTER, QuestDifficulty.getQuestDifficultyByNumber(4));
    }

    @Test
    void testGetQuestDifficultyByNumberUnhappyFlow() {
        assertEquals(QuestDifficulty.SPECIAL, QuestDifficulty.getQuestDifficultyByNumber(-1));
        assertNull(QuestDifficulty.getQuestDifficultyByNumber(5));
    }
}