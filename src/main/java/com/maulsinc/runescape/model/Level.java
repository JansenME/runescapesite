package com.maulsinc.runescape.model;

import lombok.Data;

import static com.maulsinc.runescape.CommonsService.getFormattedNumber;

@Data
public class Level {
    private Skill skill;
    private Long rank;
    private Long rankIronman;
    private Long rankHardcoreIronman;
    private Long level;
    private Long experience;
    private Long experienceToday;

    public String getFormattedRank() {
        if(experience == null || experience == -1 || experience == 0) {
            return "--";
        }
        return getFormattedNumber(rank);
    }

    public String getFormattedRankIronman() {
        if(rankIronman == null || rankIronman == -1 || rankIronman == 0) {
            return "--";
        }
        return getFormattedNumber(rankIronman);
    }

    public String getFormattedRankHardcoreIronman() {
        if(rankHardcoreIronman == null || rankHardcoreIronman == -1 || rankHardcoreIronman == 0) {
            return "--";
        }
        return getFormattedNumber(rankHardcoreIronman);
    }

    public String getFormattedLevel() {
        if(experience == null || experience == -1 || experience == 0) {
            return "--";
        }
        return getFormattedNumber(level);
    }

    public String getFormattedExperience() {
        if(experience == null || experience == -1 || experience == 0) {
            return "--";
        }
        return getFormattedNumber(experience);
    }

    public String getFormattedExperienceToday() {
        if(experienceToday == null || experienceToday == -1 || experienceToday == 0) {
            return "--";
        }
        return getFormattedNumber(experienceToday);
    }

    public static int getNumberFromSkill(Level level) {
        return level.getSkill().getNumber();
    }
}
