package com.runescape.info.model;

import com.runescape.info.CommonsService;
import lombok.Data;

@Data
public class Minigame {
    private MinigameEnum minigameEnum;
    private Long rank;
    private Long score;

    public String getFormattedRank() {
        if(rank == -1) {
            return "--";
        }

        return CommonsService.getFormattedNumber(rank);
    }

    public String getFormattedScore() {
        if(score == -1) {
            return "--";
        }

        return CommonsService.getFormattedNumber(score);
    }
}
