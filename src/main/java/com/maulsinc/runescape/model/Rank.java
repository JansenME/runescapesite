package com.maulsinc.runescape.model;

import lombok.Getter;

@Getter
public enum Rank {
    OWNER(1, "Owner"),
    DEPUTY_OWNER(2, "Deputy Owner"),
    OVERSEER(3, "Overseer"),
    COORDINATOR(4, "Coordinator"),
    ORGANISER(5, "Organiser"),
    ADMIN(6, "Admin"),
    GENERAL(7, "General"),
    CAPTAIN(8, "Captain"),
    LIEUTENANT(9, "Lieutenant"),
    SERGEANT(10, "Sergeant"),
    CORPORAL(11, "Corporal"),
    RECRUIT(12, "Recruit");

    private final int order;
    private final String name;

    Rank(final int order, final String name) {
        this.order = order;
        this.name = name;
    }

    public static Rank getEnumWithName(final String name) {
        for (Rank rank : values()) {
            if(rank.name.equals(name)) {
                return rank;
            }
        }
        return null;
    }
}
