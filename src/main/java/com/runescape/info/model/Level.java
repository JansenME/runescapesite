package com.runescape.info.model;

import lombok.Data;

@Data
public class Level {
    private Skill skill;
    private Integer rank;
    private Integer level;
    private Integer experience;
}
