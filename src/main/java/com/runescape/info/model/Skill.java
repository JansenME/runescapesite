package com.runescape.info.model;

import java.util.List;

public enum Skill {
    OVERALL(0, "Overall"),
    ATTACK(1, "Attack"),
    DEFENCE(2, "Defence"),
    STRENGTH(3, "Strength"),
    CONSTITUTION(4, "Constitution"),
    RANGED(5, "Ranged"),
    PRAYER(6, "Prayer"),
    MAGIC(7, "Magic"),
    COOKING(8, "Cooking"),
    WOODCUTTING(9, "Woodcutting"),
    FLETCHING(10, "Fletching"),
    FISHING(11, "Fishing"),
    FIREMAKING(12, "Firemaking"),
    CRAFTING(13, "Crafting"),
    SMITHING(14, "Smithing"),
    MINING(15, "Mining"),
    HERBLORE(16, "Herblore"),
    AGILITY(17, "Agility"),
    THIEVING(18, "Thieving"),
    SLAYER(19, "Slayer"),
    FARMING(20, "Farming"),
    RUNECRAFTING(21, "Runecrafting"),
    HUNTER(22, "Hunter"),
    CONSTRUCTION(23, "Construction"),
    SUMMONING(24, "Summoning"),
    DUNGEONEERING(25, "Dungeoneering"),
    DIVINATION(26, "Divination"),
    INVENTION(27, "Invention"),
    ARCHAEOLOGY(28, "Archaeology"),
    NECROMANCY(29, "Necromancy");

    private final Integer number;
    private final String name;

    Skill(final int number, final String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static String getNameWithNumber(final Integer number) {
        return List.of(values()).get(number).name;
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
