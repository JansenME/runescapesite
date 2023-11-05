package com.runescape.info.model;

import lombok.Data;

@Data
public class Level {
    private Skill skill;
    private Long rank;
    private Long level;
    private Long experience;
}
