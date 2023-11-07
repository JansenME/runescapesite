package com.runescape.info.model;

import lombok.Data;

import java.text.DecimalFormat;

@Data
public class Level {
    private Skill skill;
    private Long rank;
    private Long level;
    private Long experience;

    public String getFormattedRank() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(this.rank);
    }

    public String getFormattedLevel() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(this.level);
    }

    public String getFormattedExperience() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(this.experience);
    }
}
