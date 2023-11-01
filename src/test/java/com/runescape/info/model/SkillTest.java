package com.runescape.info.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SkillTest {

    @Test
    public void testGetNameWithNumber() {
        assertEquals("Overall", Skill.getNameWithNumber(0));
        assertEquals("Attack", Skill.getNameWithNumber(1));
        assertEquals("Defence", Skill.getNameWithNumber(2));
        assertEquals("Strength", Skill.getNameWithNumber(3));
        assertEquals("Constitution", Skill.getNameWithNumber(4));
        assertEquals("Ranged", Skill.getNameWithNumber(5));
        assertEquals("Prayer", Skill.getNameWithNumber(6));
        assertEquals("Magic", Skill.getNameWithNumber(7));
        assertEquals("Cooking", Skill.getNameWithNumber(8));
        assertEquals("Woodcutting", Skill.getNameWithNumber(9));
        assertEquals("Fletching", Skill.getNameWithNumber(10));
        assertEquals("Fishing", Skill.getNameWithNumber(11));
        assertEquals("Firemaking", Skill.getNameWithNumber(12));
        assertEquals("Crafting", Skill.getNameWithNumber(13));
        assertEquals("Smithing", Skill.getNameWithNumber(14));
        assertEquals("Mining", Skill.getNameWithNumber(15));
        assertEquals("Herblore", Skill.getNameWithNumber(16));
        assertEquals("Agility", Skill.getNameWithNumber(17));
        assertEquals("Thieving", Skill.getNameWithNumber(18));
        assertEquals("Slayer", Skill.getNameWithNumber(19));
        assertEquals("Farming", Skill.getNameWithNumber(20));
        assertEquals("Runecrafting", Skill.getNameWithNumber(21));
        assertEquals("Hunter", Skill.getNameWithNumber(22));
        assertEquals("Construction", Skill.getNameWithNumber(23));
        assertEquals("Summoning", Skill.getNameWithNumber(24));
        assertEquals("Dungeoneering", Skill.getNameWithNumber(25));
        assertEquals("Divination", Skill.getNameWithNumber(26));
        assertEquals("Invention", Skill.getNameWithNumber(27));
        assertEquals("Archaeology", Skill.getNameWithNumber(28));
        assertEquals("Necromancy", Skill.getNameWithNumber(29));
    }

    @Test
    public void testGetSkillByNumber() {
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
}