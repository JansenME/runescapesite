package com.maulsinc.runescape.service;

import com.maulsinc.runescape.model.Clanmember;
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
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    void testGetTop10RunescoreHappyFlow() {
        List<Clanmember> clanmembers = createClanmemberListForRunescore(15);

        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 1")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 1", 5000L, 450L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 2")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 2", 4658L, 35456L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 3")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 3", 12546L, 4566L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 4")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 4", 2135L, 456456L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 5")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 5", 4832L, 46554L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 6")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 6", 55458L, 456468L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 7")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 7", 212315L, 4655L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 8")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 8", 146558L, 164545L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 9")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 9", 798823L, 46555L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 10")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 10", 79540L, 789850L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 11")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 11", 452L, 13135L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 12")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 12", 135477L, 31215L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 13")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 13", 41546L, 3121550L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 14")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 14", 4889636L, 468456L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 15")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 15", 12353L, 464548L));

        List<Pair<String, String>> runescores = clanmemberMinigamesService.getTop10Runescore(clanmembers);

        assertEquals(10, runescores.size());

        assertEquals("Clanmember 13", runescores.get(0).getFirst());
        assertEquals("3.121.550", runescores.get(0).getSecond());

        assertEquals("Clanmember 10", runescores.get(1).getFirst());
        assertEquals("789.850", runescores.get(1).getSecond());

        assertEquals("Clanmember 14", runescores.get(2).getFirst());
        assertEquals("468.456", runescores.get(2).getSecond());

        assertEquals("Clanmember 15", runescores.get(3).getFirst());
        assertEquals("464.548", runescores.get(3).getSecond());

        assertEquals("Clanmember 6", runescores.get(4).getFirst());
        assertEquals("456.468", runescores.get(4).getSecond());

        assertEquals("Clanmember 4", runescores.get(5).getFirst());
        assertEquals("456.456", runescores.get(5).getSecond());

        assertEquals("Clanmember 8", runescores.get(6).getFirst());
        assertEquals("164.545", runescores.get(6).getSecond());

        assertEquals("Clanmember 9", runescores.get(7).getFirst());
        assertEquals("46.555", runescores.get(7).getSecond());

        assertEquals("Clanmember 5", runescores.get(8).getFirst());
        assertEquals("46.554", runescores.get(8).getSecond());

        assertEquals("Clanmember 2", runescores.get(9).getFirst());
        assertEquals("35.456", runescores.get(9).getSecond());
    }

    @Test
    void testGetTop10RunescoreWith0Runescores() {
        List<Clanmember> clanmembers = createClanmemberListForRunescore(10);

        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 1")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 1", 5000L, 450L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 2")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 2", 4658L, 35456L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 3")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 3", 12546L, 0L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 4")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 4", 2135L, 456456L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 5")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 5", 4832L, 0L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 6")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 6", 55458L, 0L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 7")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 7", 212315L, 4655L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 8")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 8", 146558L, 0L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 9")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 9", 798823L, 46555L));
        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 10")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 10", 79540L, 789850L));

        List<Pair<String, String>> runescores = clanmemberMinigamesService.getTop10Runescore(clanmembers);

        assertEquals(6, runescores.size());

        assertEquals("Clanmember 10", runescores.get(0).getFirst());
        assertEquals("789.850", runescores.get(0).getSecond());

        assertEquals("Clanmember 4", runescores.get(1).getFirst());
        assertEquals("456.456", runescores.get(1).getSecond());

        assertEquals("Clanmember 9", runescores.get(2).getFirst());
        assertEquals("46.555", runescores.get(2).getSecond());

        assertEquals("Clanmember 2", runescores.get(3).getFirst());
        assertEquals("35.456", runescores.get(3).getSecond());

        assertEquals("Clanmember 7", runescores.get(4).getFirst());
        assertEquals("4.655", runescores.get(4).getSecond());

        assertEquals("Clanmember 1", runescores.get(5).getFirst());
        assertEquals("450", runescores.get(5).getSecond());
    }

    @Test
    void testGetTop10RunescoreEmptyClanmemberList() {
        List<Pair<String, String>> runescores = clanmemberMinigamesService.getTop10Runescore(new ArrayList<>());

        assertEquals(0, runescores.size());
    }

    @Test
    void testGetTop10RunescoreNullClanmemberList() {
        List<Pair<String, String>> runescores = clanmemberMinigamesService.getTop10Runescore(null);

        assertEquals(0, runescores.size());
    }

    @Test
    void testGetTop10RunescoreClanmemberNameInClanmemberMinigamesNull() {
        List<Clanmember> clanmembers = createClanmemberListForRunescore(1);

        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 1")).thenReturn(createClanmemberMinigamesEntityForRunescore(null, 5000L, 450L));

        List<Pair<String, String>> runescores = clanmemberMinigamesService.getTop10Runescore(clanmembers);

        assertEquals(0, runescores.size());
    }

    @Test
    void testGetTop10RunescoreMinigamesRunescoreNullScore() {
        List<Clanmember> clanmembers = createClanmemberListForRunescore(1);

        when(clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc("Clanmember 1")).thenReturn(createClanmemberMinigamesEntityForRunescore("Clanmember 1", null, null));

        List<Pair<String, String>> runescores = clanmemberMinigamesService.getTop10Runescore(clanmembers);

        assertEquals(0, runescores.size());
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

    private List<Clanmember> createClanmemberListForRunescore(final int amount) {
        List<Clanmember> clanmembers = new ArrayList<>();

        IntStream.range(0, amount).forEach(value -> clanmembers.add(createClanmember("Clanmember " + (value+1))));

        return clanmembers;
    }

    private Clanmember createClanmember(final String name) {
        Clanmember clanmember = new Clanmember();

        clanmember.setName(name);

        return clanmember;
    }

    private ClanmemberMinigamesEntity createClanmemberMinigamesEntityForRunescore(final String name, final Long rank, final Long score) {
        ClanmemberMinigamesEntity clanmemberMinigamesEntity = new ClanmemberMinigamesEntity();

        clanmemberMinigamesEntity.setId(ObjectId.get());
        clanmemberMinigamesEntity.setClanmember(name);
        clanmemberMinigamesEntity.setMinigames(
                Stream.of(createMinigame(MinigameName.RUNESCORE, rank, score))
                        .collect(Collectors.toList())
        );

        return clanmemberMinigamesEntity;
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
        return Stream.of(
                    createMinigame(MinigameName.BOUNTY_HUNTER, 4652L, 978523L),
                    createMinigame(MinigameName.DOMINION_TOWER, 7645L, 782374L),
                    createMinigame(MinigameName.BA_HEALERS, 123L, 54363L),
                    createMinigame(MinigameName.FIST_OF_GUTHIX, 852L, 9343L),
                    createMinigame(MinigameName.HEIST_GUARD_LEVEL, 4558522L, 4546L),
                    createMinigame(MinigameName.RUNESCORE, 558221L, 52588541L))
                .collect(Collectors.toList());
    }

    private Minigame createMinigame(final MinigameName minigameName, final Long rank, final Long score) {
        Minigame minigame = new Minigame();

        minigame.setMinigameName(minigameName);
        minigame.setRank(rank);
        minigame.setScore(score);

        return minigame;
    }

}