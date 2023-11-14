package com.maulsinc.runescape.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RankTest {

    @Test
    void testGetEnumWithNameHappyFlow() {
        assertEquals(Rank.OWNER, Rank.getEnumWithName("Owner"));
        assertEquals(Rank.DEPUTY_OWNER, Rank.getEnumWithName("Deputy Owner"));
        assertEquals(Rank.OVERSEER, Rank.getEnumWithName("Overseer"));
        assertEquals(Rank.COORDINATOR, Rank.getEnumWithName("Coordinator"));
        assertEquals(Rank.ORGANISER, Rank.getEnumWithName("Organiser"));
        assertEquals(Rank.ADMIN, Rank.getEnumWithName("Admin"));
        assertEquals(Rank.GENERAL, Rank.getEnumWithName("General"));
        assertEquals(Rank.CAPTAIN, Rank.getEnumWithName("Captain"));
        assertEquals(Rank.LIEUTENANT, Rank.getEnumWithName("Lieutenant"));
        assertEquals(Rank.SERGEANT, Rank.getEnumWithName("Sergeant"));
        assertEquals(Rank.CORPORAL, Rank.getEnumWithName("Corporal"));
        assertEquals(Rank.RECRUIT, Rank.getEnumWithName("Recruit"));
    }

    @Test
    void testGetEnumWithNameUnhappyFlow() {
        assertNull(Rank.getEnumWithName("Not a correct name"));
    }
}