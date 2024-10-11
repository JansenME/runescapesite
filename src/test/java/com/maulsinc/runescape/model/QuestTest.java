package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestTest {
    @Test
    void testGetDifficultyHappyFlow() {
        Quest quest = new Quest();

        quest.setDifficulty(QuestDifficulty.GRANDMASTER);

        assertEquals(QuestDifficulty.GRANDMASTER, quest.getDifficulty());
    }

    @Test
    void testGetDifficultyNullValue() {
        Quest quest = new Quest();

        assertEquals(QuestDifficulty.SPECIAL, quest.getDifficulty());
    }
}