package com.maulsinc.runescape.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static com.maulsinc.runescape.CommonsService.getFormattedNumber;

@Data
public class Minigame {
    private MinigameName minigameName;
    private Long rank;
    private Long rankIronman;
    private Long rankHardcoreIronman;
    private Long score;

    public String getFormattedRank() {
        if(rank == -1) {
            return "--";
        }
        return getFormattedNumber(rank);
    }

    public String getFormattedRankIronman() {
        if(rankIronman == null || rankIronman == -1 || rankIronman == 0) {
            return "--";
        }
        return getFormattedNumber(rankIronman);
    }

    public String getFormattedRankHardcoreIronman() {
        if(rankHardcoreIronman == null || rankHardcoreIronman == -1 || rankHardcoreIronman == 0) {
            return "--";
        }
        return getFormattedNumber(rankHardcoreIronman);
    }

    public String getFormattedScore() {
        if(score == null || score == -1) {
            return "--";
        }

        return getFormattedNumber(score);
    }

    public String getMinigameNameString() {
        if (minigameName == null || minigameName.getName() == null) {
            return "";
        }

        return minigameName.getName();
    }

    public String getMinigameNameForImage() {
        if (minigameName == null || minigameName.getName() == null) {
            return "";
        }

        return StringUtils.deleteWhitespace(minigameName.getName().replace(":", "").replace(".", "")).toLowerCase();
    }
}
