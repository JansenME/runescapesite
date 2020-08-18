package com.runescape.info.model;

import lombok.Data;

@Data
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
}
