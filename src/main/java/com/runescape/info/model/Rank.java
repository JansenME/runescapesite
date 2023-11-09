package com.runescape.info.model;

import lombok.Getter;

@Getter
public enum Rank {
    OWNER("Owner"),
    DEPUTY_OWNER("Deputy Owner"),
    OVERSEER("Overseer"),
    COORDINATOR("Coordinator"),
    ORGANISER("Organiser"),
    ADMIN("Admin"),
    GENERAL("General"),
    CAPTAIN("Captain"),
    LIEUTENANT("Lieutenant"),
    SERGEANT("Sergeant"),
    CORPORAL("Corporal"),
    RECRUIT("Recruit");

    private final String name;

    Rank(final String name) {
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
