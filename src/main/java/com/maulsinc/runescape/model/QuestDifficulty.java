package com.maulsinc.runescape.model;

import lombok.Getter;

public enum QuestDifficulty {
    SPECIAL(-1, ""),
    NOVICE(0, "Novice"),
    INTERMEDIATE(1, "Intermediate"),
    EXPERIENCED(2, "Experienced"),
    MASTER(3, "Master"),
    GRANDMASTER(4, "Grandmaster");

    private final Integer number;
    @Getter
    private final String name;

    QuestDifficulty(final Integer number, final String name) {
        this.number = number;
        this.name = name;
    }

    public static QuestDifficulty getQuestDifficultyByNumber(final Integer number) {
        for(QuestDifficulty questDifficulty : QuestDifficulty.values()) {
            if (questDifficulty.number.equals(number)) {
                return questDifficulty;
            }
        }
        return null;
    }
}
