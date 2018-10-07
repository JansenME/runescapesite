package com.runescape.info.model;

/**
 * Created by Martijn Jansen on 6/18/2017.
 */
public class SkillsInList {
    private String name;
    private String nameUppercase;
    private String correctVirtualLevel;
    private String experienceFormatted;
    private String rankFormatted;

    private String totalVirtualLevel;
    private String color;

    public SkillsInList(String name, String correctVirtualLevel, String experienceFormatted, String rankFormatted, String totalVirtualLevel, String color) {
        this.name = name;
        this.nameUppercase = name.substring(0, 1).toUpperCase() + name.substring(1);
        this.correctVirtualLevel = correctVirtualLevel;
        this.experienceFormatted = experienceFormatted;
        this.rankFormatted = rankFormatted;
        this.totalVirtualLevel = totalVirtualLevel;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorrectVirtualLevel() {
        return correctVirtualLevel;
    }

    public void setCorrectVirtualLevel(String correctVirtualLevel) {
        this.correctVirtualLevel = correctVirtualLevel;
    }

    public String getExperienceFormatted() {
        return experienceFormatted;
    }

    public void setExperienceFormatted(String experienceFormatted) {
        this.experienceFormatted = experienceFormatted;
    }

    public String getRankFormatted() {
        return rankFormatted;
    }

    public void setRankFormatted(String rankFormatted) {
        this.rankFormatted = rankFormatted;
    }

    public String getTotalVirtualLevel() {
        return totalVirtualLevel;
    }

    public void setTotalVirtualLevel(String totalVirtualLevel) {
        this.totalVirtualLevel = totalVirtualLevel;
    }

    public String getNameUppercase() {
        return nameUppercase;
    }

    public void setNameUppercase(String nameUppercase) {
        this.nameUppercase = nameUppercase;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
