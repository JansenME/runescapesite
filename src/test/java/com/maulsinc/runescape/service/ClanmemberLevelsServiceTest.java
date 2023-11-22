package com.maulsinc.runescape.service;

import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.Level;
import com.maulsinc.runescape.model.Skill;
import com.maulsinc.runescape.repository.ClanmemberLevelsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClanmemberLevelsServiceTest {
    @Mock
    ClanmemberLevelsRepository clanmemberLevelsRepository;

    @Test
    void testGetCombatLevelHappyFlow() {
        ClanmemberLevelsService clanmemberLevelsService = new ClanmemberLevelsService(clanmemberLevelsRepository);
        ClanmemberLevels clanmemberLevels124 = createClanmemberLevels(
                38L,
                45L,
                118L,
                95L,
                120L,
                105L,
                38L,
                50L,
                50L
        );

        ClanmemberLevels clanmemberLevels138 = createClanmemberLevels(
                99L,
                99L,
                99L,
                99L,
                99L,
                99L,
                99L,
                99L,
                99L
                );

        ClanmemberLevels clanmemberLevels152 = createClanmemberLevels(
                120L,
                120L,
                120L,
                120L,
                120L,
                120L,
                120L,
                120L,
                120L
        );

        String combatLevel124 = clanmemberLevelsService.getCombatLevel(clanmemberLevels124);
        assertEquals("124", combatLevel124);

        String combatLevel138 = clanmemberLevelsService.getCombatLevel(clanmemberLevels138);
        assertEquals("138", combatLevel138);

        String combatLevel152 = clanmemberLevelsService.getCombatLevel(clanmemberLevels152);
        assertEquals("152", combatLevel152);
    }

    @Test
    void testGetCombatLevelUnhappyFlowEmptyClanmemberLevels() {
        ClanmemberLevelsService clanmemberLevelsService = new ClanmemberLevelsService(clanmemberLevelsRepository);
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        String combatLevel = clanmemberLevelsService.getCombatLevel(clanmemberLevels);

        assertEquals("--", combatLevel);
    }

    @Test
    void testGetCombatLevelUnhappyFlowEmptyClanmemberLevelsList() {
        ClanmemberLevelsService clanmemberLevelsService = new ClanmemberLevelsService(clanmemberLevelsRepository);
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();
        clanmemberLevels.setLevels(new ArrayList<>());

        String combatLevel = clanmemberLevelsService.getCombatLevel(clanmemberLevels);

        assertEquals("--", combatLevel);
    }

    private ClanmemberLevels createClanmemberLevels(
            final Long attackLevel,
            final Long strengthLevel,
            final Long magicLevel,
            final Long rangedLevel,
            final Long necromancyLevel,
            final Long defenceLevel,
            final Long constitutionLevel,
            final Long prayerLevel,
            final Long summoningLevel) {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        clanmemberLevels.setLevels(createLevelsList(
                attackLevel,
                strengthLevel,
                magicLevel,
                rangedLevel,
                necromancyLevel,
                defenceLevel,
                constitutionLevel,
                prayerLevel,
                summoningLevel
        ));

        return clanmemberLevels;
    }

    private List<Level> createLevelsList(
            final Long attackLevel,
            final Long strengthLevel,
            final Long magicLevel,
            final Long rangedLevel,
            final Long necromancyLevel,
            final Long defenceLevel,
            final Long constitutionLevel,
            final Long prayerLevel,
            final Long summoningLevel) {
        Level attack = new Level();
        attack.setSkill(Skill.ATTACK);
        attack.setLevel(attackLevel);

        Level strength = new Level();
        strength.setSkill(Skill.STRENGTH);
        strength.setLevel(strengthLevel);

        Level magic = new Level();
        magic.setSkill(Skill.MAGIC);
        magic.setLevel(magicLevel);

        Level ranged = new Level();
        ranged.setSkill(Skill.RANGED);
        ranged.setLevel(rangedLevel);

        Level necromancy = new Level();
        necromancy.setSkill(Skill.NECROMANCY);
        necromancy.setLevel(necromancyLevel);

        Level defence = new Level();
        defence.setSkill(Skill.DEFENCE);
        defence.setLevel(defenceLevel);

        Level constitution = new Level();
        constitution.setSkill(Skill.CONSTITUTION);
        constitution.setLevel(constitutionLevel);

        Level prayer = new Level();
        prayer.setSkill(Skill.PRAYER);
        prayer.setLevel(prayerLevel);

        Level summoning = new Level();
        summoning.setSkill(Skill.SUMMONING);
        summoning.setLevel(summoningLevel);

        return Stream.of(
                        attack, strength, magic, ranged, necromancy, defence, constitution, prayer, summoning
                )
                .collect(Collectors.toList());
    }
}