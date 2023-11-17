package com.maulsinc.runescape.model;

import lombok.Data;

@Data
public class Quest {
    private String title;
    private QuestStatus status;
    private QuestDifficulty difficulty;
    private boolean members;
    private Integer questPoints;
    private boolean userEligible;

    public QuestDifficulty getDifficulty() {
        if(difficulty == null) {
            return QuestDifficulty.SPECIAL;
        }

        return difficulty;
    }
}
