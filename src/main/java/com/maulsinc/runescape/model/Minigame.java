package com.maulsinc.runescape.model;

import com.maulsinc.runescape.CommonsService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

        return CommonsService.getFormattedNumber(rank);
    }

    public String getFormattedRankIronman() {
        if(rankIronman == null || rankIronman == 0) {
            return "--";
        }
        return CommonsService.getFormattedNumber(rankIronman);
    }

    public String getFormattedRankHardcoreIronman() {
        if(rankHardcoreIronman == null || rankHardcoreIronman == 0) {
            return "--";
        }
        return CommonsService.getFormattedNumber(rankHardcoreIronman);
    }

    public String getFormattedScore() {
        if(score == null || score == -1) {
            return "--";
        }

        return CommonsService.getFormattedNumber(score);
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
