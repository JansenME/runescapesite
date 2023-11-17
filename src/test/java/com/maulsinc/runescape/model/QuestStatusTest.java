package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestStatusTest {

    @Test
    void testGetEnumByTextHappyFlow() {
        assertEquals(QuestStatus.NOT_STARTED, QuestStatus.getEnumByText("NOT_STARTED"));
        assertEquals(QuestStatus.STARTED, QuestStatus.getEnumByText("STARTED"));
        assertEquals(QuestStatus.COMPLETED, QuestStatus.getEnumByText("COMPLETED"));
    }

    @Test
    void testGetEnumByTextUnhappyFlow() {
        assertEquals(QuestStatus.UNKNOWN, QuestStatus.getEnumByText("Huppeldepup"));
    }

}