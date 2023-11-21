package com.maulsinc.runescape.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public enum MinigameName {
    BOUNTY_HUNTER(0, "Bounty Hunter"),
    BH_ROGUES(1, "B.H. Rogues"),
    DOMINION_TOWER(2, "Dominion Tower"),
    THE_CRUCIBLE(3, "The Crucible"),
    CASTLE_WARS_GAMES(4, "Castle Wars games"),
    BA_ATTACKERS(5, "B.A. Attackers"),
    BA_DEFENDERS(6, "B.A. Defenders"),
    BA_COLLECTORS(7, "B.A. Collectors"),
    BA_HEALERS(8, "B.A. Healers"),
    DUEL_TOURNAMENT(9, "Duel Tournament"),
    MOBILISING_ARMIES(10, "Mobilising Armies"),
    CONQUEST(11, "Conquest"),
    FIST_OF_GUTHIX(12, "Fist of Guthix"),
    GG_ATHLETICS(13, "GG: Athletics"),
    GG_RESOURCE_RACE(14, "GG: Resource Race"),
    WE2_ARMADYL_LIFETIME_CONTRIBUTION(15, "WE2: Armadyl Lifetime Contribution"),
    WE2_BANDOS_LIFETIME_CONTRIBUTION(16, "WE2: Bandos Lifetime Contribution"),
    WE2_ARMADYL_PVP_KILLS(17, "WE2: Armadyl PvP kills"),
    WE2_BANDOS_PVP_KILLS(18, "WE2: Bandos PvP kills"),
    HEIST_GUARD_LEVEL(19, "Heist Guard Level"),
    HEIST_ROBBER_LEVEL(20, "Heist Robber Level"),
    CFP_5_GAME_AVERAGE(21, "CFP: 5 game average"),
    AF15_COW_TIPPING(22, "AF15: Cow Tipping"),
    AF15_RATS_KILLED_AFTER_THE_MINIQUEST(23, "AF15: Rats killed after the miniquest"),
    RUNESCORE(24, "RuneScore"),
    CLUE_SCROLLS_EASY(25, "Clue Scrolls Easy"),
    CLUE_SCROLLS_MEDIUM(26, "Clue Scrolls Medium"),
    CLUE_SCROLLS_HARD(27, "Clue Scrolls Hard"),
    CLUE_SCROLLS_ELITE(28, "Clue Scrolls Elite"),
    CLUE_SCROLLS_MASTER(29, "Clue Scrolls Master");

    private final Integer number;
    @Getter
    private final String name;

    MinigameName(final int number, final String name) {
        this.number = number;
        this.name = name;
    }

    public static MinigameName getMinigameNameByNumber(final Integer number) {
        for(MinigameName minigameName : MinigameName.values()) {
            if (minigameName.number.equals(number)) {
                return minigameName;
            }
        }
        return null;
    }
}
