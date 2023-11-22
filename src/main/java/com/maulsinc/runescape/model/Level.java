package com.maulsinc.runescape.model;

import com.maulsinc.runescape.CommonsService;
import lombok.Data;

@Data
public class Level {
    private Skill skill;
    private Long rank;
    private Long level;
    private Long experience;
    private Long experienceToday;

    public String getFormattedRank() {
        if(experience == null || experience == -1 || experience == 0) {
            return "--";
        }
        return CommonsService.getFormattedNumber(rank);
    }

    public String getFormattedLevel() {
        if(experience == null || experience == -1 || experience == 0) {
            return "--";
        }
        return CommonsService.getFormattedNumber(level);
    }

    public String getFormattedExperience() {
        if(experience == null || experience == -1 || experience == 0) {
            return "--";
        }
        return CommonsService.getFormattedNumber(experience);
    }

    public String getFormattedExperienceToday() {
        if(experienceToday == null || experienceToday == -1 || experienceToday == 0) {
            return "--";
        }
        return CommonsService.getFormattedNumber(experienceToday);
    }

    public static int getNumberFromSkill(Level level) {
        return level.getSkill().getNumber();
    }
}
