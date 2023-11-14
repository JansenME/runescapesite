package com.runescape.info.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SkillTest {

    @Test
    void testGetSkillByNumberHappyFlow() {
        assertEquals(Skill.OVERALL, Skill.getSkillByNumber(0));
        assertEquals(Skill.ATTACK, Skill.getSkillByNumber(1));
        assertEquals(Skill.DEFENCE, Skill.getSkillByNumber(2));
        assertEquals(Skill.STRENGTH, Skill.getSkillByNumber(3));
        assertEquals(Skill.CONSTITUTION, Skill.getSkillByNumber(4));
        assertEquals(Skill.RANGED, Skill.getSkillByNumber(5));
        assertEquals(Skill.PRAYER, Skill.getSkillByNumber(6));
        assertEquals(Skill.MAGIC, Skill.getSkillByNumber(7));
        assertEquals(Skill.COOKING, Skill.getSkillByNumber(8));
        assertEquals(Skill.WOODCUTTING, Skill.getSkillByNumber(9));
        assertEquals(Skill.FLETCHING, Skill.getSkillByNumber(10));
        assertEquals(Skill.FISHING, Skill.getSkillByNumber(11));
        assertEquals(Skill.FIREMAKING, Skill.getSkillByNumber(12));
        assertEquals(Skill.CRAFTING, Skill.getSkillByNumber(13));
        assertEquals(Skill.SMITHING, Skill.getSkillByNumber(14));
        assertEquals(Skill.MINING, Skill.getSkillByNumber(15));
        assertEquals(Skill.HERBLORE, Skill.getSkillByNumber(16));
        assertEquals(Skill.AGILITY, Skill.getSkillByNumber(17));
        assertEquals(Skill.THIEVING, Skill.getSkillByNumber(18));
        assertEquals(Skill.SLAYER, Skill.getSkillByNumber(19));
        assertEquals(Skill.FARMING, Skill.getSkillByNumber(20));
        assertEquals(Skill.RUNECRAFTING, Skill.getSkillByNumber(21));
        assertEquals(Skill.HUNTER, Skill.getSkillByNumber(22));
        assertEquals(Skill.CONSTRUCTION, Skill.getSkillByNumber(23));
        assertEquals(Skill.SUMMONING, Skill.getSkillByNumber(24));
        assertEquals(Skill.DUNGEONEERING, Skill.getSkillByNumber(25));
        assertEquals(Skill.DIVINATION, Skill.getSkillByNumber(26));
        assertEquals(Skill.INVENTION, Skill.getSkillByNumber(27));
        assertEquals(Skill.ARCHAEOLOGY, Skill.getSkillByNumber(28));
        assertEquals(Skill.NECROMANCY, Skill.getSkillByNumber(29));
    }

    @Test
    void testGetSkillByNumberUnhappyFlow() {
        assertNull(Skill.getSkillByNumber(-1));
        assertNull(Skill.getSkillByNumber(30));
    }
}