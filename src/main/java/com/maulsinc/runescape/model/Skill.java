package com.maulsinc.runescape.model;

import lombok.Getter;

@Getter
public enum Skill {
    OVERALL(0, "Overall", false),
    ATTACK(1, "Attack", false),
    DEFENCE(2, "Defence", false),
    STRENGTH(3, "Strength", false),
    CONSTITUTION(4, "Constitution", false),
    RANGED(5, "Ranged", false),
    PRAYER(6, "Prayer", false),
    MAGIC(7, "Magic", false),
    COOKING(8, "Cooking", false),
    WOODCUTTING(9, "Woodcutting", false),
    FLETCHING(10, "Fletching", false),
    FISHING(11, "Fishing", false),
    FIREMAKING(12, "Firemaking", false),
    CRAFTING(13, "Crafting", false),
    SMITHING(14, "Smithing", false),
    MINING(15, "Mining", false),
    HERBLORE(16, "Herblore", false),
    AGILITY(17, "Agility", false),
    THIEVING(18, "Thieving", false),
    SLAYER(19, "Slayer", false),
    FARMING(20, "Farming", false),
    RUNECRAFTING(21, "Runecrafting", false),
    HUNTER(22, "Hunter", false),
    CONSTRUCTION(23, "Construction", false),
    SUMMONING(24, "Summoning", false),
    DUNGEONEERING(25, "Dungeoneering", false),
    DIVINATION(26, "Divination", false),
    INVENTION(27, "Invention", true),
    ARCHAEOLOGY(28, "Archaeology", false),
    NECROMANCY(29, "Necromancy", false);

    private final Integer number;
    private final String name;
    private final boolean eliteSkill;

    Skill(final int number, final String name, final boolean eliteSkill) {
        this.number = number;
        this.name = name;
        this.eliteSkill = eliteSkill;
    }

    public static Skill getSkillByNumber(final Integer number) {
        for(Skill skill : Skill.values()) {
            if (skill.number.equals(number)) {
                return skill;
            }
        }
        return null;
    }
}
