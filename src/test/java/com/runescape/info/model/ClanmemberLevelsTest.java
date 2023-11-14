package com.runescape.info.model;

import com.runescape.info.model.entity.ClanmemberLevelsEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClanmemberLevelsTest {

    @Test
    void testMapEntityToModel() {
        String date = "10-11-2023 10:00 a.m. CET";
        ClanmemberLevels clanmemberLevels = ClanmemberLevels.mapEntityToModel(createEntity(), date);

        assertEquals("name", clanmemberLevels.getClanmember());
        assertEquals(Skill.AGILITY, clanmemberLevels.getLevels().get(0).getSkill());
        assertEquals(date, clanmemberLevels.getDate());
    }

    private ClanmemberLevelsEntity createEntity() {
        ClanmemberLevelsEntity entity = new ClanmemberLevelsEntity();

        Level level = new Level();
        level.setSkill(Skill.AGILITY);

        entity.setClanmember("name");
        entity.setLevels(new ArrayList<>(
                List.of(level)
        ));

        return entity;
    }
}