package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maulsinc.runescape.model.Clanmember;
import com.maulsinc.runescape.model.ClanmemberQuests;
import com.maulsinc.runescape.model.Rank;
import com.maulsinc.runescape.model.entity.ClanmembersEntity;
import com.maulsinc.runescape.repository.ClanmembersRepository;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
class ClanmembersServiceTest {
    static ObjectMapper mapper;

    @InjectMocks ClanmembersService clanmembersService;

    @Mock ConnectionService connectionService;
    @Mock ClanmemberLevelsService clanmemberLevelsService;
    @Mock ClanmemberMinigamesService clanmemberMinigamesService;
    @Mock ClanmemberQuestsService clanmemberQuestsService;
    @Mock ClanmembersRepository clanmembersRepository;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void testSaveAllClanmembersFromRunescapeHappyFlow() {
        List<CSVRecord> records = new ArrayList<>();
        records.add(createMockCsvRecordClanmember("Clanmate", "Clan Rank", "Total XP", "Kills"));
        records.add(createMockCsvRecordClanmember("Clanmate 1", "General", "85548852", "4"));
        records.add(createMockCsvRecordClanmember("Clanmate 2", "Overseer", "123554855", "2"));

        when(connectionService.getCSVRecordsFromRunescapeForClan()).thenReturn(records);

        records.remove(0);
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(Clanmember.mapCsvRecordsToClanmembers(records));
        when(clanmembersRepository.save(any())).thenReturn(clanmembersEntity);

        clanmembersService.saveAllClanmembersFromRunescape();

        verify(clanmembersRepository, times(1)).save(any());
    }

    @Test
    void testSaveAllClanmembersFromRunescapeClanmembersEntityNullList() {
        when(connectionService.getCSVRecordsFromRunescapeForClan()).thenReturn(null);

        clanmembersService.saveAllClanmembersFromRunescape();

        verify(clanmembersRepository, times(0)).save(any());
    }

    @Test
    void testSaveAllClanmembersFromRunescapeClanmembersEntityEmptyList() {
        when(connectionService.getCSVRecordsFromRunescapeForClan()).thenReturn(new ArrayList<>());

        clanmembersService.saveAllClanmembersFromRunescape();

        verify(clanmembersRepository, times(0)).save(any());
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberInformationHappyFlow() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        List<CSVRecord> records = createValidClanmemberInformationList();

        when(connectionService.getJsonNodeFromRunescapeForClanmemberProfile(any())).thenReturn(createValidJsonNode());
        when(connectionService.getCSVRecordsFromRunescapeForClanmember(any())).thenReturn(records);

        clanmembersService.getAllClanmembersAndSaveClanmemberInformation();

        verify(clanmemberLevelsService, times(4)).saveClanmemberLevelsToDatabaseFromProfile(any(), any());
        verify(clanmemberMinigamesService, times(4)).saveClanmemberMinigamesToDatabase(any(), any());
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberInformationRecordsNot60Size() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        List<CSVRecord> records = new ArrayList<>();
        records.add(createMockCsvRecordLevel("500", "81", "15658855"));

        when(connectionService.getJsonNodeFromRunescapeForClanmemberProfile(any())).thenReturn(createValidJsonNode());
        when(connectionService.getCSVRecordsFromRunescapeForClanmember(any())).thenReturn(records);

        clanmembersService.getAllClanmembersAndSaveClanmemberInformation();

        verify(clanmemberLevelsService, times(0)).saveClanmemberLevelsToDatabaseFromProfile(any(), any());
        verify(clanmemberMinigamesService, times(0)).saveClanmemberMinigamesToDatabase(any(), any());
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberInformationRecordsEmptyList() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        when(connectionService.getJsonNodeFromRunescapeForClanmemberProfile(any())).thenReturn(createValidJsonNode());
        when(connectionService.getCSVRecordsFromRunescapeForClanmember(any())).thenReturn(new ArrayList<>());

        clanmembersService.getAllClanmembersAndSaveClanmemberInformation();

        verify(clanmemberLevelsService, times(0)).saveClanmemberLevelsToDatabaseFromProfile(any(), any());
        verify(clanmemberMinigamesService, times(0)).saveClanmemberMinigamesToDatabase(any(), any());
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberInformationJsonNodeEmpty() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        List<CSVRecord> records = createValidClanmemberInformationList();

        when(connectionService.getJsonNodeFromRunescapeForClanmemberProfile(any())).thenReturn(createEmptyJsonNode());
        when(connectionService.getCSVRecordsFromRunescapeForClanmember(any())).thenReturn(records);

        clanmembersService.getAllClanmembersAndSaveClanmemberInformation();

        verify(clanmemberLevelsService, times(0)).saveClanmemberLevelsToDatabaseFromProfile(any(), any());
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberInformationJsonNodeHasError() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        List<CSVRecord> records = createValidClanmemberInformationList();

        when(connectionService.getJsonNodeFromRunescapeForClanmemberProfile(any())).thenReturn(createErrorJsonNode());
        when(connectionService.getCSVRecordsFromRunescapeForClanmember(any())).thenReturn(records);

        clanmembersService.getAllClanmembersAndSaveClanmemberInformation();

        verify(clanmemberLevelsService, times(0)).saveClanmemberLevelsToDatabaseFromProfile(any(), any());
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberQuestsHappyFlow() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("quests", "Quests");

        when(connectionService.getJsonNodeFromRunescapeForClanmemberQuests(any())).thenReturn(objectNode);

        clanmembersService.getAllClanmembersAndSaveClanmemberQuests();
    }

    @Test
    void testGetAllClanmembersAndSaveClanmemberQuestsJsonNodeNullNode() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        when(connectionService.getJsonNodeFromRunescapeForClanmemberQuests(any())).thenReturn(NullNode.getInstance());

        clanmembersService.getAllClanmembersAndSaveClanmemberQuests();
    }

    @Test
    void testGetAllClanmembersHappyFlow() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(createClanmemberList());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        Pair<String, List<Clanmember>> clanmembers = clanmembersService.getAllClanmembers();

        assertEquals(4, clanmembers.getSecond().size());

        assertEquals(Rank.CAPTAIN, clanmembers.getSecond().get(1).getRank());
        assertEquals(2155455L, clanmembers.getSecond().get(1).getTotalXP());

        assertEquals(Rank.OWNER, clanmembers.getSecond().get(3).getRank());
        assertEquals(10L, clanmembers.getSecond().get(3).getKills());
    }

    @Test
    void testGetAllClanmembersClanmembersNull() {
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(null);

        Pair<String, List<Clanmember>> clanmembers = clanmembersService.getAllClanmembers();

        assertEquals("", clanmembers.getFirst());
        assertEquals(0, clanmembers.getSecond().size());
    }

    @Test
    void testGetAllClanmembersClanmembersEmptyList() {
        ClanmembersEntity clanmembersEntity = new ClanmembersEntity(new ArrayList<>());
        clanmembersEntity.setId(ObjectId.get());
        when(clanmembersRepository.findFirstByOrderByIdDesc()).thenReturn(clanmembersEntity);

        Pair<String, List<Clanmember>> clanmembers = clanmembersService.getAllClanmembers();

        assertEquals(0, clanmembers.getSecond().size());
    }

    private JsonNode createValidJsonNode() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("skillvalues", "Lots of nice things");

        return objectNode;
    }

    private JsonNode createEmptyJsonNode() {
        ObjectNode objectNode = mapper.createObjectNode();

        return objectNode;
    }

    private JsonNode createErrorJsonNode() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("error", "Ow no...");

        return objectNode;
    }

    private List<CSVRecord> createValidClanmemberInformationList() {
        List<CSVRecord> records = new ArrayList<>();

        IntStream.range(0, 30)
                .forEach(value -> records.add(createMockCsvRecordLevel(randomNumberGenerator(50000), randomNumberGenerator(120), randomNumberGenerator(200000000))));

        IntStream.range(0, 30)
                .forEach(value -> records.add(createMockCsvRecordMinigame(randomNumberGenerator(50000), randomNumberGenerator(100000))));

        return records;
    }

    private String randomNumberGenerator(final int max) {
        Random random = new Random();

        return String.valueOf(random.nextInt(max));
    }

    private CSVRecord createMockCsvRecordLevel(final String rank, final String level, final String experience) {
        CSVRecord record = mock(CSVRecord.class);
        Mockito.lenient().when(record.get(0)).thenReturn(rank);
        Mockito.lenient().when(record.get(1)).thenReturn(level);
        Mockito.lenient().when(record.get(2)).thenReturn(experience);

        return record;
    }

    private CSVRecord createMockCsvRecordMinigame(final String rank, final String score) {
        CSVRecord record = mock(CSVRecord.class);
        Mockito.lenient().when(record.get(0)).thenReturn(rank);
        Mockito.lenient().when(record.get(1)).thenReturn(score);

        return record;
    }

    private List<Clanmember> createClanmemberList() {
        List<Clanmember> clanmembers = new ArrayList<>();
        clanmembers.add(createClanmember("Clanmember 1", Rank.OVERSEER, 500542L, 1L));
        clanmembers.add(createClanmember("Clanmember 2", Rank.CAPTAIN, 2155455L, 5L));
        clanmembers.add(createClanmember("Clanmember 3", Rank.GENERAL, 5044885L, 21L));
        clanmembers.add(createClanmember("Clanmember 4", Rank.OWNER, 4564642L, 10L));

        return clanmembers;
    }

    private Clanmember createClanmember(final String name, final Rank rank, final Long totalXP, final Long kills) {
        Clanmember clanmember = new Clanmember();
        clanmember.setName(name);
        clanmember.setRank(rank);
        clanmember.setTotalXP(totalXP);
        clanmember.setKills(kills);

        return clanmember;
    }

    private CSVRecord createMockCsvRecordClanmember(final String clanmate, final String clanRank, final String totalXp, final String kills) {
        CSVRecord record = mock(CSVRecord.class);
        Mockito.lenient().when(record.get(0)).thenReturn(clanmate);
        Mockito.lenient().when(record.get(1)).thenReturn(clanRank);
        Mockito.lenient().when(record.get(2)).thenReturn(totalXp);
        Mockito.lenient().when(record.get(3)).thenReturn(kills);

        return record;
    }
}