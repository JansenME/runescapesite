package com.maulsinc.runescape.service;

import com.maulsinc.runescape.model.ClanmemberMinigames;
import com.maulsinc.runescape.model.Minigame;
import com.maulsinc.runescape.model.MinigameName;
import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import com.maulsinc.runescape.repository.ClanmemberMinigamesRepository;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
class ClanmemberMinigamesServiceTest {
    private static final String NAME = "HC_Kloeperd";

    @Mock
    ClanmemberMinigamesRepository clanmemberMinigamesRepository;

    @InjectMocks
    ClanmemberMinigamesService clanmemberMinigamesService;

    @Test
    void testGetOneClanmemberMinigamesHappyFlow() {
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc(anyString())).thenReturn(createClanmemberMinigamesEntity());

        ClanmemberMinigames clanmemberMinigames = clanmemberMinigamesService.getOneClanmemberMinigames(NAME);

        assertEquals(NAME, clanmemberMinigames.getClanmember());
        assertEquals(6, clanmemberMinigames.getMinigames().size());

        assertEquals(MinigameName.BOUNTY_HUNTER, clanmemberMinigames.getMinigames().get(0).getMinigameName());
        assertEquals(4652L, clanmemberMinigames.getMinigames().get(0).getRank());
        assertEquals(978523L, clanmemberMinigames.getMinigames().get(0).getScore());

        assertEquals(MinigameName.BA_HEALERS, clanmemberMinigames.getMinigames().get(2).getMinigameName());
        assertEquals(123L, clanmemberMinigames.getMinigames().get(2).getRank());
        assertEquals(54363L, clanmemberMinigames.getMinigames().get(2).getScore());
    }

    @Test
    void testGetOneClanmemberMinigamesEntityNull() {
        ClanmemberMinigames clanmemberMinigames = clanmemberMinigamesService.getOneClanmemberMinigames("NAME_NOT_IN_DATABASE");

        assertNull(clanmemberMinigames.getClanmember());
        assertNull(clanmemberMinigames.getMinigames());
        assertNull(clanmemberMinigames.getDate());
    }

    @Test
    void testSaveClanmemberMinigamesToDatabaseHappyFlow() {
        List<CSVRecord> minigames = List.of(createMockCsvRecord("5456", "54564"));
        List<CSVRecord> minigamesIronman = List.of(createMockCsvRecord("5456", "54564"));
        List<CSVRecord> minigamesHardcoreIronman = List.of(createMockCsvRecord("5456", "54564"));

        clanmemberMinigamesService.saveClanmemberMinigamesToDatabase(NAME, minigames, minigamesIronman, minigamesHardcoreIronman);

        verify(clanmemberMinigamesRepository, times(1)).save(any());
    }

    @Test
    void testSaveClanmemberMinigamesToDatabaseClanmemberNull() {
        List<CSVRecord> minigames = List.of(createMockCsvRecord("5456", "54564"));

        clanmemberMinigamesService.saveClanmemberMinigamesToDatabase(null, minigames, new ArrayList<>(), new ArrayList<>());

        verify(clanmemberMinigamesRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberMinigamesToDatabaseMinigamesNull() {
        clanmemberMinigamesService.saveClanmemberMinigamesToDatabase(NAME, null, new ArrayList<>(), new ArrayList<>());

        verify(clanmemberMinigamesRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberMinigamesToDatabaseMinigamesEmptyList() {
        clanmemberMinigamesService.saveClanmemberMinigamesToDatabase(NAME, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        verify(clanmemberMinigamesRepository, times(0)).save(any());
    }

    @Test
    void testGetRunescoreMinigameHappyFlow() {
        List<Minigame> minigames = List.of(
                createMinigame(MinigameName.BA_COLLECTORS, 5885L, 545868L),
                createMinigame(MinigameName.RUNESCORE, 1234L, 56789L),
                createMinigame(MinigameName.FIST_OF_GUTHIX, 5887L, 1234568L)
        );

        ClanmemberMinigames clanmemberMinigames = new ClanmemberMinigames();
        clanmemberMinigames.setClanmember(NAME);
        clanmemberMinigames.setMinigames(minigames);

        Minigame result = clanmemberMinigamesService.getRunescoreMinigame(clanmemberMinigames);

        assertEquals(MinigameName.RUNESCORE, result.getMinigameName());
        assertEquals(1234L, result.getRank());
        assertEquals(56789L, result.getScore());
    }

    @Test
    void testGetRunescoreMinigameClanmemberMinigamesNull() {
        Minigame result = clanmemberMinigamesService.getRunescoreMinigame(null);

        assertNull(result.getMinigameName());
        assertNull(result.getRank());
        assertNull(result.getScore());
    }

    @Test
    void testGetRunescoreMinigameClanmemberMinigamesGetMinigamesNull() {
        ClanmemberMinigames clanmemberMinigames = new ClanmemberMinigames();
        clanmemberMinigames.setClanmember(NAME);
        clanmemberMinigames.setMinigames(null);

        Minigame result = clanmemberMinigamesService.getRunescoreMinigame(clanmemberMinigames);

        assertNull(result.getMinigameName());
        assertNull(result.getRank());
        assertNull(result.getScore());
    }

    @Test
    void testGetRunescoreMinigameClanmemberMinigamesGetMinigamesEmptyList() {
        ClanmemberMinigames clanmemberMinigames = new ClanmemberMinigames();
        clanmemberMinigames.setClanmember(NAME);
        clanmemberMinigames.setMinigames(new ArrayList<>());

        Minigame result = clanmemberMinigamesService.getRunescoreMinigame(clanmemberMinigames);

        assertNull(result.getMinigameName());
        assertNull(result.getRank());
        assertNull(result.getScore());
    }

    @Test
    void testGetRunescoreMinigameClanmemberMinigamesGetMinigamesListWithoutRunescore() {
        List<Minigame> minigames = List.of(
                createMinigame(MinigameName.BA_COLLECTORS, 5885L, 545868L),
                createMinigame(MinigameName.FIST_OF_GUTHIX, 5887L, 1234568L)
        );

        ClanmemberMinigames clanmemberMinigames = new ClanmemberMinigames();
        clanmemberMinigames.setClanmember(NAME);
        clanmemberMinigames.setMinigames(minigames);

        Minigame result = clanmemberMinigamesService.getRunescoreMinigame(clanmemberMinigames);

        assertNull(result.getMinigameName());
        assertNull(result.getRank());
        assertNull(result.getScore());
    }

    @Test
    void testGetClanmemberMinigamesEntityHappyFlow() {
        List<CSVRecord> minigames = List.of(
                createMockCsvRecord("5498", "56247542"),
                createMockCsvRecord("4756", "625438652"),
                createMockCsvRecord("485769", "426457"),
                createMockCsvRecord("5647", "24567452"),
                createMockCsvRecord("3456", "8256856"),
                createMockCsvRecord("2345", "48465"),
                createMockCsvRecord("1234", "957676")
        );

        List<CSVRecord> minigamesIronman = List.of(
                createMockCsvRecord("5498", "56247542"),
                createMockCsvRecord("4756", "625438652"),
                createMockCsvRecord("485769", "426457"),
                createMockCsvRecord("5647", "24567452"),
                createMockCsvRecord("3456", "8256856"),
                createMockCsvRecord("2345", "48465"),
                createMockCsvRecord("1234", "957676"));

        List<CSVRecord> minigamesHardcoreIronman = List.of(
                createMockCsvRecord("5498", "56247542"),
                createMockCsvRecord("4756", "625438652"),
                createMockCsvRecord("485769", "426457"),
                createMockCsvRecord("5647", "24567452"),
                createMockCsvRecord("3456", "8256856"),
                createMockCsvRecord("2345", "48465"),
                createMockCsvRecord("1234", "957676"));

        ClanmemberMinigamesEntity clanmemberMinigamesEntity = clanmemberMinigamesService.getClanmemberMinigamesEntity(NAME, minigames, minigamesIronman, minigamesHardcoreIronman);

        assertEquals(7, clanmemberMinigamesEntity.getMinigames().size());

        assertEquals(MinigameName.BOUNTY_HUNTER, clanmemberMinigamesEntity.getMinigames().get(0).getMinigameName());
        assertEquals(5498L, clanmemberMinigamesEntity.getMinigames().get(0).getRank());
        assertEquals("5.498", clanmemberMinigamesEntity.getMinigames().get(0).getFormattedRank());
        assertEquals(56247542L, clanmemberMinigamesEntity.getMinigames().get(0).getScore());
        assertEquals("56.247.542", clanmemberMinigamesEntity.getMinigames().get(0).getFormattedScore());

        assertEquals(MinigameName.THE_CRUCIBLE, clanmemberMinigamesEntity.getMinigames().get(3).getMinigameName());
        assertEquals(5647L, clanmemberMinigamesEntity.getMinigames().get(3).getRank());
        assertEquals("5.647", clanmemberMinigamesEntity.getMinigames().get(3).getFormattedRank());
        assertEquals(24567452L, clanmemberMinigamesEntity.getMinigames().get(3).getScore());
        assertEquals("24.567.452", clanmemberMinigamesEntity.getMinigames().get(3).getFormattedScore());
    }

    private Minigame createMinigame(final MinigameName minigameName, final Long rank, final Long score) {
        Minigame minigame = new Minigame();

        minigame.setMinigameName(minigameName);
        minigame.setRank(rank);
        minigame.setScore(score);

        return minigame;
    }

    private CSVRecord createMockCsvRecord(final String rank, final String score) {
        CSVRecord record = mock(CSVRecord.class);
        Mockito.lenient().when(record.get(0)).thenReturn(rank);
        Mockito.lenient().when(record.get(1)).thenReturn(score);

        return record;
    }

    private ClanmemberMinigamesEntity createClanmemberMinigamesEntity() {
        ClanmemberMinigamesEntity clanmemberMinigamesEntity = new ClanmemberMinigamesEntity();

        clanmemberMinigamesEntity.setId(ObjectId.get());
        clanmemberMinigamesEntity.setClanmember(NAME);
        clanmemberMinigamesEntity.setMinigames(createMinigamesList());

        return clanmemberMinigamesEntity;
    }

    private List<Minigame> createMinigamesList() {
        Minigame bountyHunter = new Minigame();
        bountyHunter.setMinigameName(MinigameName.BOUNTY_HUNTER);
        bountyHunter.setRank(4652L);
        bountyHunter.setScore(978523L);

        Minigame dominionTower = new Minigame();
        dominionTower.setMinigameName(MinigameName.DOMINION_TOWER);
        dominionTower.setRank(7645L);
        dominionTower.setScore(782374L);

        Minigame baHealers = new Minigame();
        baHealers.setMinigameName(MinigameName.BA_HEALERS);
        baHealers.setRank(123L);
        baHealers.setScore(54363L);

        Minigame fistOfGuthix = new Minigame();
        fistOfGuthix.setMinigameName(MinigameName.FIST_OF_GUTHIX);
        fistOfGuthix.setRank(852L);
        fistOfGuthix.setScore(9343L);

        Minigame heistGuardLevel = new Minigame();
        heistGuardLevel.setMinigameName(MinigameName.HEIST_GUARD_LEVEL);
        heistGuardLevel.setRank(4558522L);
        heistGuardLevel.setScore(4546L);

        Minigame runescore = new Minigame();
        runescore.setMinigameName(MinigameName.RUNESCORE);
        runescore.setRank(558221L);
        runescore.setScore(52588541L);

        return Stream.of(bountyHunter, dominionTower, baHealers, fistOfGuthix, heistGuardLevel, runescore)
                .collect(Collectors.toList());
    }

}