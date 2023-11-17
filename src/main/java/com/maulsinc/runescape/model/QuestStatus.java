package com.maulsinc.runescape.model;

import lombok.Getter;

public enum QuestStatus {
    UNKNOWN("Unknown"),
    NOT_STARTED("Not started"),
    STARTED("Started"),
    COMPLETED("Completed");

    @Getter
    private final String name;

    QuestStatus(final String name) {
        this.name = name;
    }

    public static QuestStatus getEnumByText(final String text) {
        try {
            return QuestStatus.valueOf(text);
        } catch (IllegalArgumentException e) {
            return QuestStatus.UNKNOWN;
        }
    }
}
