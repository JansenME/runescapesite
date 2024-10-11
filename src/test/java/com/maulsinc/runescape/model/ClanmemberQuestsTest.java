package com.maulsinc.runescape.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClanmemberQuestsTest {
    static ClanmemberQuests clanmemberQuests;

    @BeforeAll
    static void setup() {
        clanmemberQuests = new ClanmemberQuests();
    }

    @Test
    void testGetTotalQuestPointsAsStringHappyFlow() {
        clanmemberQuests.setTotalQuestPoints(500);

        String totalQuestPoints = clanmemberQuests.getTotalQuestPointsAsString();

        assertEquals("500",totalQuestPoints);
    }

    @Test
    void testGetTotalQuestPointsAsStringTotalQuestPointsNull() {
        clanmemberQuests.setTotalQuestPoints(null);

        String totalQuestPoints = clanmemberQuests.getTotalQuestPointsAsString();

        assertEquals("--",totalQuestPoints);
    }

    @Test
    void testGetTotalQuestPointsAsStringTotalQuestPointsMinusOne() {
        clanmemberQuests.setTotalQuestPoints(-1);

        String totalQuestPoints = clanmemberQuests.getTotalQuestPointsAsString();

        assertEquals("--",totalQuestPoints);
    }

    @Test
    void testGetTotalQuestPointsAsStringTotalQuestPointsZero() {
        clanmemberQuests.setTotalQuestPoints(0);

        String totalQuestPoints = clanmemberQuests.getTotalQuestPointsAsString();

        assertEquals("--",totalQuestPoints);
    }
}