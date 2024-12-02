package com.maulsinc.runescape.service;

import com.maulsinc.runescape.model.Clanmember;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.Level;
import com.maulsinc.runescape.model.entity.ClanmembersTop5ExperienceEntity;
import com.maulsinc.runescape.repository.ClanmembersTop5ExperienceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
class ClanmembersTop5ExperienceServiceTest {
    @InjectMocks ClanmembersTop5ExperienceService clanmembersTop5ExperienceService;

    @Mock ClanmembersTop5ExperienceRepository clanmembersTop5ExperienceRepository;
    @Mock ClanmemberLevelsService clanmemberLevelsService;

    @Test
    void testSaveClanmembersTop5ExperienceHappyFlow() {
        List<Clanmember> clanmembers = createValidClanmemberList();

        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(0).getName())).thenReturn(createClanmemberLevels(clanmembers.get(0).getName(), 501325L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(1).getName())).thenReturn(createClanmemberLevels(clanmembers.get(1).getName(), 45885L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(2).getName())).thenReturn(createClanmemberLevels(clanmembers.get(2).getName(), 31255L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(3).getName())).thenReturn(createClanmemberLevels(clanmembers.get(3).getName(), 123588L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(4).getName())).thenReturn(createClanmemberLevels(clanmembers.get(4).getName(), 789635L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(5).getName())).thenReturn(createClanmemberLevels(clanmembers.get(5).getName(), 81444553L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(6).getName())).thenReturn(createClanmemberLevels(clanmembers.get(6).getName(), 6999965L));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(7).getName())).thenReturn(createClanmemberLevels(clanmembers.get(7).getName(), 254585465L));

        clanmembersTop5ExperienceService.saveClanmembersTop5Experience(clanmembers);

        verify(clanmembersTop5ExperienceRepository, times(1)).save(any());
    }

    @Test
    void testSaveClanmembersTop5ExperienceEmptyLevelsListInClanmemberLevels() {
        List<Clanmember> clanmembers = createValidClanmemberList();

        when(clanmemberLevelsService.getOneClanmemberLevels(any())).thenReturn(createEmptyClanmemberLevels(clanmembers.get(0).getName()));

        clanmembersTop5ExperienceService.saveClanmembersTop5Experience(clanmembers);

        verify(clanmembersTop5ExperienceRepository, times(1)).save(any());
    }

    @Test
    void testSaveClanmembersTop5ExperienceNullExperienceTodayInClanmemberLevels() {
        List<Clanmember> clanmembers = createValidClanmemberList();

        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(0).getName())).thenReturn(createClanmemberLevels(clanmembers.get(0).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(1).getName())).thenReturn(createClanmemberLevels(clanmembers.get(1).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(2).getName())).thenReturn(createClanmemberLevels(clanmembers.get(2).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(3).getName())).thenReturn(createClanmemberLevels(clanmembers.get(3).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(4).getName())).thenReturn(createClanmemberLevels(clanmembers.get(4).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(5).getName())).thenReturn(createClanmemberLevels(clanmembers.get(5).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(6).getName())).thenReturn(createClanmemberLevels(clanmembers.get(6).getName(), null));
        when(clanmemberLevelsService.getOneClanmemberLevels(clanmembers.get(7).getName())).thenReturn(createClanmemberLevels(clanmembers.get(7).getName(), null));

        clanmembersTop5ExperienceService.saveClanmembersTop5Experience(clanmembers);

        verify(clanmembersTop5ExperienceRepository, times(1)).save(any());
    }

    @Test
    void testGetClanmembersTop5ExperienceEntityHappyFlow() {
        List<Clanmember> clanmembers = createValidClanmemberList();

        List<ClanmemberLevels> clanmemberLevels = new ArrayList<>();
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(0).getName(), 501325L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(1).getName(), 45885L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(2).getName(), 31255L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(3).getName(), 123588L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(4).getName(), 789635L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(5).getName(), 81444553L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(6).getName(), 6999965L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(7).getName(), 254585465L));

        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = clanmembersTop5ExperienceService.createClanmembersTop5ExperienceEntity(clanmemberLevels);

        assertEquals(5, clanmembersTop5ExperienceEntity.getClanmemberLevels().size());

        assertEquals(clanmembers.get(7).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(0).getClanmember());
        assertEquals(254585465L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(0).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(5).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(1).getClanmember());
        assertEquals(81444553L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(1).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(6).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(2).getClanmember());
        assertEquals(6999965L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(2).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(4).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(3).getClanmember());
        assertEquals(789635L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(3).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(0).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(4).getClanmember());
        assertEquals(501325L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(4).getLevels().get(0).getExperienceToday());
    }

    @Test
    void testGetClanmembersTop5ExperienceEntityListWith3ValidValues() {
        List<Clanmember> clanmembers = createValidClanmemberList();

        List<ClanmemberLevels> clanmemberLevels = new ArrayList<>();
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(0).getName(), 0L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(1).getName(), 0L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(2).getName(), 0L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(3).getName(), 0L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(4).getName(), 0L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(5).getName(), 81444553L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(6).getName(), 6999965L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(7).getName(), 254585465L));

        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = clanmembersTop5ExperienceService.createClanmembersTop5ExperienceEntity(clanmemberLevels);

        assertEquals(3, clanmembersTop5ExperienceEntity.getClanmemberLevels().size());

        assertEquals(clanmembers.get(7).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(0).getClanmember());
        assertEquals(254585465L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(0).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(5).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(1).getClanmember());
        assertEquals(81444553L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(1).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(6).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(2).getClanmember());
        assertEquals(6999965L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(2).getLevels().get(0).getExperienceToday());
    }

    @Test
    void testGetClanmembersTop5ExperienceEntityNullExperienceToday() {
        List<Clanmember> clanmembers = createValidClanmemberList();

        List<ClanmemberLevels> clanmemberLevels = new ArrayList<>();
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(0).getName(), null));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(1).getName(), null));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(2).getName(), null));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(3).getName(), null));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(4).getName(), null));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(5).getName(), 81444553L));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(6).getName(), null));
        clanmemberLevels.add(createClanmemberLevels(clanmembers.get(7).getName(), 254585465L));

        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = clanmembersTop5ExperienceService.createClanmembersTop5ExperienceEntity(clanmemberLevels);

        assertEquals(2, clanmembersTop5ExperienceEntity.getClanmemberLevels().size());

        assertEquals(clanmembers.get(7).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(0).getClanmember());
        assertEquals(254585465L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(0).getLevels().get(0).getExperienceToday());

        assertEquals(clanmembers.get(5).getName(), clanmembersTop5ExperienceEntity.getClanmemberLevels().get(1).getClanmember());
        assertEquals(81444553L, clanmembersTop5ExperienceEntity.getClanmemberLevels().get(1).getLevels().get(0).getExperienceToday());
    }

    @Test
    void testGetClanmembersTop5ExperienceEntityEmptyList() {
        List<ClanmemberLevels> clanmemberLevels = new ArrayList<>();

        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = clanmembersTop5ExperienceService.createClanmembersTop5ExperienceEntity(clanmemberLevels);

        assertEquals(0, clanmembersTop5ExperienceEntity.getClanmemberLevels().size());
    }

    @Test
    void testGetClanmembersTop5ExperienceEntityNullList() {
        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = clanmembersTop5ExperienceService.createClanmembersTop5ExperienceEntity(null);

        assertEquals(0, clanmembersTop5ExperienceEntity.getClanmemberLevels().size());
    }

    @Test
    void testGetClanmembersTop5ExperienceHappyFlow() {
        when(clanmembersTop5ExperienceRepository.findFirstByOrderByIdDesc()).thenReturn(createValidEntity());

        List<ClanmemberLevels> clanmemberLevels = clanmembersTop5ExperienceService.getClanmembersTop5Experience();

        assertEquals(1, clanmemberLevels.size());
        assertEquals("Clanmember 1", clanmemberLevels.get(0).getClanmember());
    }

    @Test
    void testGetClanmembersTop5ExperienceEmptyEntityList() {
        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = new ClanmembersTop5ExperienceEntity();

        clanmembersTop5ExperienceEntity.setClanmemberLevels(new ArrayList<>());

        when(clanmembersTop5ExperienceRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersTop5ExperienceEntity);

        List<ClanmemberLevels> clanmemberLevels = clanmembersTop5ExperienceService.getClanmembersTop5Experience();

        assertEquals(0, clanmemberLevels.size());
    }

    @Test
    void testGetClanmembersTop5ExperienceNullEntityList() {
        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = new ClanmembersTop5ExperienceEntity();

        clanmembersTop5ExperienceEntity.setClanmemberLevels(null);

        when(clanmembersTop5ExperienceRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersTop5ExperienceEntity);

        List<ClanmemberLevels> clanmemberLevels = clanmembersTop5ExperienceService.getClanmembersTop5Experience();

        assertEquals(0, clanmemberLevels.size());
    }

    @Test
    void testGetClanmembersTop5ExperienceNullEntity() {
        when(clanmembersTop5ExperienceRepository.findFirstByOrderByIdDesc()).thenReturn(null);

        List<ClanmemberLevels> clanmemberLevels = clanmembersTop5ExperienceService.getClanmembersTop5Experience();

        assertEquals(0, clanmemberLevels.size());
    }

    private List<Clanmember> createValidClanmemberList() {
        return List.of(
                createClanmember("player 1"),
                createClanmember("player 2"),
                createClanmember("player 3"),
                createClanmember("player 4"),
                createClanmember("player 5"),
                createClanmember("player 6"),
                createClanmember("player 7"),
                createClanmember("player 8")
        );
    }

    private Clanmember createClanmember(final String name) {
        Clanmember clanmember = new Clanmember();

        clanmember.setName(name);

        return clanmember;
    }

    private ClanmemberLevels createClanmemberLevels(final String name, final Long totalExperienceToday) {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        clanmemberLevels.setClanmember(name);
        clanmemberLevels.setLevels(createLevelList(totalExperienceToday));

        return clanmemberLevels;
    }

    private List<Level> createLevelList(final Long totalExperienceToday) {
         return List.of(
                 createLevel(totalExperienceToday)
         );
    }

    private Level createLevel(final Long totalExperienceToday) {
        Level level = new Level();
        level.setExperienceToday(totalExperienceToday);

        return level;
    }

    private ClanmemberLevels createEmptyClanmemberLevels(final String name) {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        clanmemberLevels.setClanmember(name);
        clanmemberLevels.setLevels(Collections.emptyList());

        return clanmemberLevels;
    }

    private ClanmembersTop5ExperienceEntity createValidEntity() {
        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = new ClanmembersTop5ExperienceEntity();

        List<ClanmemberLevels> clanmemberLevels = new ArrayList<>();

        clanmemberLevels.add(createClanmemberLevels("Clanmember 1", 50554468L));

        clanmembersTop5ExperienceEntity.setClanmemberLevels(clanmemberLevels);

        return clanmembersTop5ExperienceEntity;
    }
}