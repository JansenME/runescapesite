package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClanmemberMinigamesTest {

    @Test
    void testMapEntityToModel() {
        String date = "10-11-2023 10:00 a.m. CET";

        ClanmemberMinigames clanmemberMinigames = ClanmemberMinigames.mapEntityToModel(createEntity(), date);

        assertEquals("name", clanmemberMinigames.getClanmember());
        assertEquals(MinigameEnum.BA_COLLECTORS, clanmemberMinigames.getMinigames().get(0).getMinigameEnum());
        assertEquals(50L, clanmemberMinigames.getMinigames().get(0).getRank());
        assertEquals(5000L, clanmemberMinigames.getMinigames().get(0).getScore());
        assertEquals(date, clanmemberMinigames.getDate());
    }

    private ClanmemberMinigamesEntity createEntity() {
        ClanmemberMinigamesEntity entity = new ClanmemberMinigamesEntity();

        Minigame minigame = new Minigame();
        minigame.setMinigameEnum(MinigameEnum.BA_COLLECTORS);
        minigame.setRank(50L);
        minigame.setScore(5000L);

        entity.setClanmember("name");
        entity.setMinigames(new ArrayList<>(
                List.of(minigame)
        ));

        return entity;
    }
}