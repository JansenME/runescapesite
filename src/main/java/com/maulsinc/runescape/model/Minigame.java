package com.maulsinc.runescape.model;

import com.maulsinc.runescape.CommonsService;
import lombok.Data;

@Data
public class Minigame {
    private MinigameName minigameName;
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
