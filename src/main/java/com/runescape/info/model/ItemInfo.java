package com.runescape.info.model;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ItemInfo {
    private final String itemIconSmall;
    private final String itemDescription;
    private final String itemIconLarge;
    private final String itemName;
    private final String itemType;
    private final boolean itemTodayTrend;
    private final String itemTodayPrice;
    private final String itemMembers;
    private final boolean itemDay30Trend;
    private final String itemDay30Change;
    private final boolean itemDay90Trend;
    private final String itemDay90Change;
    private final boolean itemDay180Trend;
    private final String itemDay180Change;
    private final String itemPrice;
    private final String itemExperience;
    private final int itemLevelNeeded;
    private final String itemSkill;
}
