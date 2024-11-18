package com.maulsinc.runescape.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.Level;
import com.maulsinc.runescape.model.Skill;
import com.maulsinc.runescape.model.entity.ClanmemberLevelsEntity;
import com.maulsinc.runescape.repository.ClanmemberLevelsRepository;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
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
class ClanmemberLevelsServiceTest {
    private static final String NAME = "HC_Kloeperd";
    static ObjectMapper mapper;

    @Mock
    ClanmemberLevelsRepository clanmemberLevelsRepository;

    @InjectMocks
    ClanmemberLevelsService clanmemberLevelsService;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileHappyFlow() throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree("[{\"level\":120,\"xp\":1043888436,\"rank\":82731,\"id\":27},{\"level\":115,\"xp\":645700924,\"rank\":100328,\"id\":15},{\"level\":99,\"xp\":584599483,\"rank\":73750,\"id\":1},{\"level\":99,\"xp\":411957403,\"rank\":44484,\"id\":11},{\"level\":100,\"xp\":386337342,\"rank\":174601,\"id\":26},{\"level\":99,\"xp\":376249520,\"rank\":48595,\"id\":16},{\"level\":99,\"xp\":349063413,\"rank\":146402,\"id\":3},{\"level\":102,\"xp\":189060871,\"rank\":161626,\"id\":18},{\"level\":99,\"xp\":188751486,\"rank\":70961,\"id\":25},{\"level\":99,\"xp\":187922988,\"rank\":154236,\"id\":6},{\"level\":102,\"xp\":176985509,\"rank\":130822,\"id\":14},{\"level\":99,\"xp\":161109526,\"rank\":102702,\"id\":12},{\"level\":99,\"xp\":154734807,\"rank\":107295,\"id\":7},{\"level\":99,\"xp\":152945716,\"rank\":169872,\"id\":2},{\"level\":99,\"xp\":150859886,\"rank\":120349,\"id\":5},{\"level\":100,\"xp\":147289290,\"rank\":140898,\"id\":13},{\"level\":99,\"xp\":145759497,\"rank\":166852,\"id\":10},{\"level\":100,\"xp\":145451346,\"rank\":167795,\"id\":19},{\"level\":99,\"xp\":144682196,\"rank\":180672,\"id\":0},{\"level\":99,\"xp\":138816738,\"rank\":130737,\"id\":23},{\"level\":99,\"xp\":138029706,\"rank\":143886,\"id\":21},{\"level\":99,\"xp\":137729209,\"rank\":154658,\"id\":17},{\"level\":99,\"xp\":135521354,\"rank\":181026,\"id\":24},{\"level\":99,\"xp\":135209499,\"rank\":158993,\"id\":9},{\"level\":99,\"xp\":135136814,\"rank\":162656,\"id\":22},{\"level\":99,\"xp\":134427035,\"rank\":195847,\"id\":8},{\"level\":99,\"xp\":132842566,\"rank\":150555,\"id\":20},{\"level\":99,\"xp\":132260350,\"rank\":241364,\"id\":4},{\"level\":1,\"xp\":0,\"rank\":0,\"id\":28}]");

        List<CSVRecord> levels = List.of(
                createMockCsvRecord("38", "75", "45343"),
                createMockCsvRecord("7356", "24", "520055"),
                createMockCsvRecord("54667", "24", "520055"),
                createMockCsvRecord("32656", "24", "520055"),
                createMockCsvRecord("456", "24", "520055"),
                createMockCsvRecord("356", "24", "520055"),
                createMockCsvRecord("7856", "24", "520055"),
                createMockCsvRecord("789", "24", "520055"),
                createMockCsvRecord("321", "24", "520055"),
                createMockCsvRecord("432", "24", "520055"),
                createMockCsvRecord("5452", "24", "520055"),
                createMockCsvRecord("564773", "24", "520055"),
                createMockCsvRecord("8766", "24", "520055"),
                createMockCsvRecord("897", "24", "520055"),
                createMockCsvRecord("3211", "24", "520055"),
                createMockCsvRecord("456445", "24", "520055"),
                createMockCsvRecord("231354", "24", "520055"),
                createMockCsvRecord("789656", "24", "520055"),
                createMockCsvRecord("54654", "24", "520055"),
                createMockCsvRecord("735", "24", "520055"),
                createMockCsvRecord("4563", "24", "520055"),
                createMockCsvRecord("7864", "24", "520055"),
                createMockCsvRecord("6854", "24", "520055"),
                createMockCsvRecord("5767", "24", "520055"),
                createMockCsvRecord("9785", "24", "520055"),
                createMockCsvRecord("79852", "24", "520055"),
                createMockCsvRecord("3217", "24", "520055"),
                createMockCsvRecord("4567", "24", "520055"),
                createMockCsvRecord("648", "24", "520055"),
                createMockCsvRecord("2357", "24", "520055"),
                createMockCsvRecord("582", "76", "50045")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntityFromProfile(NAME, jsonNode, 50758225L, 4585L, levels, levels);

        List<Level> levelList = clanmemberLevelsEntity.getLevels();

        Level levelArchaeology = levelList.stream()
                .filter(level -> Skill.ARCHAEOLOGY.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(Skill.ARCHAEOLOGY, levelArchaeology.getSkill());
        assertEquals(120, levelArchaeology.getLevel());
        assertEquals(104388843, levelArchaeology.getExperience());
        assertEquals("104.388.843", levelArchaeology.getFormattedExperience());
        assertEquals(82731, levelArchaeology.getRank());

        Level levelMining = levelList.stream()
                .filter(level -> Skill.MINING.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(Skill.MINING, levelMining.getSkill());
        assertEquals(102, levelMining.getLevel());
        assertEquals(17698550, levelMining.getExperience());
        assertEquals("17.698.550", levelMining.getFormattedExperience());
        assertEquals(130822, levelMining.getRank());

        Level levelSmithing = levelList.stream()
                .filter(level -> Skill.SMITHING.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(Skill.SMITHING, levelSmithing.getSkill());
        assertEquals(100, levelSmithing.getLevel());
        assertEquals(14728929, levelSmithing.getExperience());
        assertEquals("14.728.929", levelSmithing.getFormattedExperience());
        assertEquals(140898, levelSmithing.getRank());

        Level levelDungeoneering = levelList.stream()
                .filter(level -> Skill.DUNGEONEERING.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(Skill.DUNGEONEERING, levelDungeoneering.getSkill());
        assertEquals(99, levelDungeoneering.getLevel());
        assertEquals(13552135, levelDungeoneering.getExperience());
        assertEquals("13.552.135", levelDungeoneering.getFormattedExperience());
        assertEquals(181026, levelDungeoneering.getRank());

        Level levelNecromancy = levelList.stream()
                .filter(level -> Skill.NECROMANCY.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(Skill.NECROMANCY, levelNecromancy.getSkill());
        assertEquals(1, levelNecromancy.getLevel());
        assertEquals(0, levelNecromancy.getExperience());
        assertEquals("--", levelNecromancy.getFormattedExperience());
        assertEquals(0, levelNecromancy.getRank());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileRankAsText() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("HenkVerhoek", "75", "45343"),
                createMockCsvRecord("7356", "24", "520055"),
                createMockCsvRecord("582", "76", "50045")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        assertEquals(2, clanmemberLevelsEntity.getLevels().size());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileLevelAsText() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("4566", "HenkVerhoek", "45343"),
                createMockCsvRecord("7356", "24", "520055"),
                createMockCsvRecord("582", "76", "50045")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        assertEquals(2, clanmemberLevelsEntity.getLevels().size());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileExperienceAsText() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("7985", "75", "HenkVerhoek"),
                createMockCsvRecord("7356", "24", "520055"),
                createMockCsvRecord("582", "76", "50045")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        assertEquals(2, clanmemberLevelsEntity.getLevels().size());
    }

    @Test
    void testGetClanMemberLevelsEntityNullSkillFromEnum() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("38", "75", "45343"),
                createMockCsvRecord("7356", "24", "520055"),
                createMockCsvRecord("54667", "24", "520055"),
                createMockCsvRecord("32656", "24", "520055"),
                createMockCsvRecord("456", "24", "520055"),
                createMockCsvRecord("356", "24", "520055"),
                createMockCsvRecord("7856", "24", "520055"),
                createMockCsvRecord("789", "24", "520055"),
                createMockCsvRecord("321", "24", "520055"),
                createMockCsvRecord("432", "24", "520055"),
                createMockCsvRecord("5452", "24", "520055"),
                createMockCsvRecord("564773", "24", "520055"),
                createMockCsvRecord("8766", "24", "520055"),
                createMockCsvRecord("897", "24", "520055"),
                createMockCsvRecord("3211", "24", "520055"),
                createMockCsvRecord("456445", "24", "520055"),
                createMockCsvRecord("231354", "24", "520055"),
                createMockCsvRecord("789656", "24", "520055"),
                createMockCsvRecord("54654", "24", "520055"),
                createMockCsvRecord("735", "24", "520055"),
                createMockCsvRecord("4563", "24", "520055"),
                createMockCsvRecord("7864", "24", "520055"),
                createMockCsvRecord("6854", "24", "520055"),
                createMockCsvRecord("5767", "24", "520055"),
                createMockCsvRecord("9785", "24", "520055"),
                createMockCsvRecord("79852", "24", "520055"),
                createMockCsvRecord("3217", "24", "520055"),
                createMockCsvRecord("4567", "24", "520055"),
                createMockCsvRecord("648", "24", "520055"),
                createMockCsvRecord("2357", "24", "520055"),
                createMockCsvRecord("582", "76", "50045")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        Level lastLevel = clanmemberLevelsEntity.getLevels().get(clanmemberLevelsEntity.getLevels().size() - 1);

        assertEquals(76, lastLevel.getLevel());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileNullSkillvalues() {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntityFromProfile(NAME, null, 5000L, 50L, new ArrayList<>(), new ArrayList<>());

        assertEquals(NAME, clanmemberLevelsEntity.getClanmember());
        assertEquals(0, clanmemberLevelsEntity.getLevels().size());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileNullTotalxp() {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntityFromProfile(NAME, mapper.createObjectNode(), null, 50L, new ArrayList<>(), new ArrayList<>());

        assertEquals(Skill.OVERALL, clanmemberLevelsEntity.getLevels().get(0).getSkill());
        assertEquals(NAME, clanmemberLevelsEntity.getClanmember());
        assertEquals(50L, clanmemberLevelsEntity.getLevels().get(0).getRank());
        assertNull(clanmemberLevelsEntity.getLevels().get(0).getExperience());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileNullRank() {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntityFromProfile(NAME, mapper.createObjectNode(), 5000L, null, new ArrayList<>(), new ArrayList<>());

        assertEquals(Skill.OVERALL, clanmemberLevelsEntity.getLevels().get(0).getSkill());
        assertEquals(NAME, clanmemberLevelsEntity.getClanmember());
        assertEquals(5000L, clanmemberLevelsEntity.getLevels().get(0).getExperience());
        assertNull(clanmemberLevelsEntity.getLevels().get(0).getRank());
    }

    @Test
    void testGetClanmemberLevelsEntityFromProfileNullClanmember() {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntityFromProfile(null, mapper.createObjectNode(), 5000L, 50L, new ArrayList<>(), new ArrayList<>());

        assertNull(clanmemberLevelsEntity.getClanmember());
    }

    @Test
    void testSaveClanmemberLevelsToDatabaseFromProfileNullClanmember() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode
                .put("skillvalues", "value")
                .put("totalxp", "50000")
                .put("rank", "5,525");

        clanmemberLevelsService.saveClanmemberLevelsToDatabaseFromProfile(null, objectNode, new ArrayList<>(), new ArrayList<>());

        verify(clanmemberLevelsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberLevelsToDatabaseFromProfileSaveMethodHappyFlow() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode
                .put("skillvalues", "value")
                .put("totalxp", "50000")
                .put("rank", "5,525");

        clanmemberLevelsService.saveClanmemberLevelsToDatabaseFromProfile(NAME, objectNode, new ArrayList<>(), new ArrayList<>());

        verify(clanmemberLevelsRepository, times(1)).save(any());
    }

    @Test
    void testSaveClanmemberLevelsToDatabaseFromProfileNullJsonNode() {
        clanmemberLevelsService.saveClanmemberLevelsToDatabaseFromProfile(NAME, null, new ArrayList<>(), new ArrayList<>());

        verify(clanmemberLevelsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanMemberLevelsToDatabaseFromProfileEmptyJsonNode() {
        clanmemberLevelsService.saveClanmemberLevelsToDatabaseFromProfile(NAME, mapper.createObjectNode(), new ArrayList<>(), new ArrayList<>());

        verify(clanmemberLevelsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanMemberLevelsToDatabaseFromProfileNullNode() {
        clanmemberLevelsService.saveClanmemberLevelsToDatabaseFromProfile(NAME, mapper.nullNode(), new ArrayList<>(), new ArrayList<>());

        verify(clanmemberLevelsRepository, times(0)).save(any());
    }

    @Test
    void testGetOverallSkillHappyFlow() {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        List<Level> levels = createLevelsList(
                98L,
                97L,
                99L,
                78L,
                124L,
                120L,
                120L,
                99L,
                100L
        );

        Level overall = new Level();
        overall.setSkill(Skill.OVERALL);
        overall.setLevel(2041L);
        overall.setExperience(5000L);

        levels.add(overall);

        clanmemberLevels.setClanmember(NAME);
        clanmemberLevels.setLevels(levels);

        Level result = clanmemberLevelsService.getOverallSkill(clanmemberLevels);

        assertEquals(Skill.OVERALL, result.getSkill());
        assertEquals(2041L, result.getLevel());
        assertEquals(5000L, result.getExperience());
    }

    @Test
    void testGetOverallSkillNoOverallSkillAvailable() {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        List<Level> levels = createLevelsList(
                98L,
                97L,
                99L,
                78L,
                124L,
                120L,
                120L,
                99L,
                100L
        );

        clanmemberLevels.setClanmember(NAME);
        clanmemberLevels.setLevels(levels);

        Level result = clanmemberLevelsService.getOverallSkill(clanmemberLevels);

        assertNull(result.getLevel());
        assertNull(result.getSkill());
        assertNull(result.getRank());
    }

    @Test
    void testGetOverallSkillEmptyLevelsList() {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        clanmemberLevels.setClanmember(NAME);
        clanmemberLevels.setLevels(new ArrayList<>());

        Level result = clanmemberLevelsService.getOverallSkill(clanmemberLevels);

        assertNull(result.getLevel());
        assertNull(result.getSkill());
        assertNull(result.getRank());
    }

    @Test
    void testGetOverallSkillNullLevelsList() {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        clanmemberLevels.setClanmember(NAME);

        Level result = clanmemberLevelsService.getOverallSkill(clanmemberLevels);

        assertNull(result.getLevel());
        assertNull(result.getSkill());
        assertNull(result.getRank());
    }

    @Test
    void testGetOverallSkillNullClanmemberLevels() {
        Level result = clanmemberLevelsService.getOverallSkill(null);

        assertNull(result.getLevel());
        assertNull(result.getSkill());
        assertNull(result.getRank());
    }

    @Test
    void testSaveClanmemberLevelsToDatabaseUnhappyFlow() {
        clanmemberLevelsService.saveClanmemberLevelsToDatabase("", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        verify(clanmemberLevelsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberLevelsToDatabaseHappyFlow() {
        List<CSVRecord> levels = List.of(createMockCsvRecord("1", "1", "1"));

        clanmemberLevelsService.saveClanmemberLevelsToDatabase(NAME, levels, levels, levels);

        verify(clanmemberLevelsRepository, times(1)).save(any());
    }

    @Test
    void testSetTotalLevelViaGetClanMemberLevelsEntityHappyFlow() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("38", "75", "45343"),
                createMockCsvRecord("5886", "24", "520055"),
                createMockCsvRecord("582", "76", "50045")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        Level levelOverall = clanmemberLevelsEntity.getLevels().stream()
                .filter(level -> Skill.OVERALL.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(100L, levelOverall.getLevel());
    }

    @Test
    void testGetCorrectLevelForEliteSkillViaGetClanMemberLevelsEntityHappyFlow() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("38", "75", "45343"),
                createMockCsvRecord("74566", "24", "520055"),
                createMockCsvRecord("63754", "24", "520055"),
                createMockCsvRecord("9875", "24", "520055"),
                createMockCsvRecord("5756", "24", "520055"),
                createMockCsvRecord("56433", "24", "520055"),
                createMockCsvRecord("8567", "24", "520055"),
                createMockCsvRecord("6498", "24", "520055"),
                createMockCsvRecord("21315", "24", "520055"),
                createMockCsvRecord("56436", "24", "520055"),
                createMockCsvRecord("6545", "24", "520055"),
                createMockCsvRecord("324532", "24", "520055"),
                createMockCsvRecord("4325", "24", "520055"),
                createMockCsvRecord("4325432", "24", "520055"),
                createMockCsvRecord("4325", "24", "520055"),
                createMockCsvRecord("3251", "24", "520055"),
                createMockCsvRecord("89765", "24", "520055"),
                createMockCsvRecord("5867", "24", "520055"),
                createMockCsvRecord("7685", "24", "520055"),
                createMockCsvRecord("9786", "24", "520055"),
                createMockCsvRecord("87655", "24", "520055"),
                createMockCsvRecord("786589", "24", "520055"),
                createMockCsvRecord("347865", "24", "520055"),
                createMockCsvRecord("745678", "24", "520055"),
                createMockCsvRecord("24654", "24", "520055"),
                createMockCsvRecord("45769", "24", "520055"),
                createMockCsvRecord("14325", "24", "520055"),
                createMockCsvRecord("125431", "24", "83370445"),
                createMockCsvRecord("45764", "24", "520055"),
                createMockCsvRecord("582", "76", "520055")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        Level levelInvention = clanmemberLevelsEntity.getLevels().stream()
                .filter(level -> Skill.INVENTION.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(121L, levelInvention.getLevel());
    }

    @Test
    void testCompleteHappyFlowForGetClanMemberLevelsEntity() {
        List<CSVRecord> levels = List.of(
                createMockCsvRecord("38", "75", "45343"),
                createMockCsvRecord("74566", "65", "3854858"),
                createMockCsvRecord("63754", "45", "6895051"),
                createMockCsvRecord("9875", "87", "10528947"),
                createMockCsvRecord("5756", "96", "520055"),
                createMockCsvRecord("56433", "99", "14052578"),
                createMockCsvRecord("8567", "98", "520055"),
                createMockCsvRecord("6498", "9", "520055"),
                createMockCsvRecord("21315", "65", "520055"),
                createMockCsvRecord("56436", "65", "520055"),
                createMockCsvRecord("6545", "23", "520055"),
                createMockCsvRecord("32452", "66", "520055"),
                createMockCsvRecord("4325", "78", "520055"),
                createMockCsvRecord("43432", "99", "15785968"),
                createMockCsvRecord("55856", "47", "520055"),
                createMockCsvRecord("3251", "24", "520055"),
                createMockCsvRecord("89765", "65", "520055"),
                createMockCsvRecord("5867", "87", "520055"),
                createMockCsvRecord("7685", "97", "520055"),
                createMockCsvRecord("9786", "99", "31789875"),
                createMockCsvRecord("87655", "87", "520055"),
                createMockCsvRecord("786589", "98", "520055"),
                createMockCsvRecord("347865", "56", "520055"),
                createMockCsvRecord("745678", "76", "520055"),
                createMockCsvRecord("24654", "28", "520055"),
                createMockCsvRecord("45769", "78", "520055"),
                createMockCsvRecord("14325", "89", "520055"),
                createMockCsvRecord("125431", "98", "83300445"),
                createMockCsvRecord("45764", "87", "520055"),
                createMockCsvRecord("582", "76", "520055")
        );

        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsService.getClanmemberLevelsEntity(NAME, levels, levels, levels);

        Level levelOverall = clanmemberLevelsEntity.getLevels().stream()
                .filter(level -> Skill.OVERALL.equals(level.getSkill()))
                .toList().get(0);

        Level levelCrafting = clanmemberLevelsEntity.getLevels().stream()
                .filter(level -> Skill.CRAFTING.equals(level.getSkill()))
                .toList().get(0);

        Level levelSlayer = clanmemberLevelsEntity.getLevels().stream()
                .filter(level -> Skill.SLAYER.equals(level.getSkill()))
                .toList().get(0);

        assertEquals(2097L, levelOverall.getLevel());
        assertEquals(100L, levelCrafting.getLevel());
        assertEquals(108L, levelSlayer.getLevel());
    }

    @Test
    void testSetExperienceTodayHappyFlow() {
        when(clanmemberLevelsRepository.findFirstByClanmemberOrderByIdDesc(anyString())).thenReturn(createClanmemberLevelsEntity());
        when(clanmemberLevelsRepository.findByObjectIdsAndAllClanmemberLevelsByClanmember(anyString(), any(), any())).thenReturn(createClanmemberLevelsEntityList());

        ClanmemberLevels clanmemberLevels = clanmemberLevelsService.getOneClanmemberLevels(NAME);

        assertEquals(NAME, clanmemberLevels.getClanmember());
        assertEquals(3000L, clanmemberLevels.getLevels().get(0).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(1).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(2).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(3).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(4).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(5).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(6).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(7).getExperienceToday());
        assertEquals(3000L, clanmemberLevels.getLevels().get(8).getExperienceToday());
    }

    @Test
    void testGetOneClanmemberLevelsHappyFlow() {
        when(clanmemberLevelsRepository.findFirstByClanmemberOrderByIdDesc(anyString())).thenReturn(createClanmemberLevelsEntity());

        ClanmemberLevels clanmemberLevels = clanmemberLevelsService.getOneClanmemberLevels(NAME);

        assertEquals(NAME, clanmemberLevels.getClanmember());
        assertEquals(99, clanmemberLevels.getLevels().stream().filter(level -> Skill.MAGIC.equals(level.getSkill())).findFirst().get().getLevel());
        assertEquals(124, clanmemberLevels.getLevels().stream().filter(level -> Skill.NECROMANCY.equals(level.getSkill())).findFirst().get().getLevel());
    }

    @Test
    void testGetOneClanmemberLevelsUnhappyFlow() {
        ClanmemberLevels clanmemberLevels = clanmemberLevelsService.getOneClanmemberLevels("NAME_NOT_IN_DATABASE");

        assertNull(clanmemberLevels.getClanmember());
        assertNull(clanmemberLevels.getLevels());
        assertNull(clanmemberLevels.getDate());
    }

    @Test
    void testGetCombatLevelHappyFlow() {
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
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();

        String combatLevel = clanmemberLevelsService.getCombatLevel(clanmemberLevels);

        assertEquals("--", combatLevel);
    }

    @Test
    void testGetCombatLevelUnhappyFlowEmptyClanmemberLevelsList() {
        ClanmemberLevels clanmemberLevels = new ClanmemberLevels();
        clanmemberLevels.setLevels(new ArrayList<>());

        String combatLevel = clanmemberLevelsService.getCombatLevel(clanmemberLevels);

        assertEquals("--", combatLevel);
    }

    private List<ClanmemberLevelsEntity> createClanmemberLevelsEntityList() {
        ClanmemberLevelsEntity clanmemberLevelsEntity = new ClanmemberLevelsEntity();

        clanmemberLevelsEntity.setId(ObjectId.get());
        clanmemberLevelsEntity.setClanmember(NAME);

        clanmemberLevelsEntity.setLevels(createLevelsListEarlier());

        List<ClanmemberLevelsEntity> out = new ArrayList<>();
        out.add(clanmemberLevelsEntity);

        return out;
    }

    private ClanmemberLevelsEntity createClanmemberLevelsEntity() {
        ClanmemberLevelsEntity clanmemberLevelsEntity = new ClanmemberLevelsEntity();

        clanmemberLevelsEntity.setId(ObjectId.get());
        clanmemberLevelsEntity.setClanmember(NAME);
        clanmemberLevelsEntity.setLevels(createLevelsList(
                98L,
                97L,
                99L,
                78L,
                124L,
                120L,
                120L,
                99L,
                100L));

        return clanmemberLevelsEntity;
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

    private List<Level> createLevelsListEarlier() {
        List<Level> levels = createLevelsList(
                98L,
                97L,
                99L,
                78L,
                124L,
                120L,
                120L,
                99L,
                100L);

        levels.forEach(level -> level.setExperience(2000L));

        return levels;
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
        attack.setExperience(5000L);

        Level strength = new Level();
        strength.setSkill(Skill.STRENGTH);
        strength.setLevel(strengthLevel);
        strength.setExperience(5000L);

        Level magic = new Level();
        magic.setSkill(Skill.MAGIC);
        magic.setLevel(magicLevel);
        magic.setExperience(5000L);

        Level ranged = new Level();
        ranged.setSkill(Skill.RANGED);
        ranged.setLevel(rangedLevel);
        ranged.setExperience(5000L);

        Level necromancy = new Level();
        necromancy.setSkill(Skill.NECROMANCY);
        necromancy.setLevel(necromancyLevel);
        necromancy.setExperience(5000L);

        Level defence = new Level();
        defence.setSkill(Skill.DEFENCE);
        defence.setLevel(defenceLevel);
        defence.setExperience(5000L);

        Level constitution = new Level();
        constitution.setSkill(Skill.CONSTITUTION);
        constitution.setLevel(constitutionLevel);
        constitution.setExperience(5000L);

        Level prayer = new Level();
        prayer.setSkill(Skill.PRAYER);
        prayer.setLevel(prayerLevel);
        prayer.setExperience(5000L);

        Level summoning = new Level();
        summoning.setSkill(Skill.SUMMONING);
        summoning.setLevel(summoningLevel);
        summoning.setExperience(5000L);

        return Stream.of(
                        attack, strength, magic, ranged, necromancy, defence, constitution, prayer, summoning
                )
                .collect(Collectors.toList());
    }

    private CSVRecord createMockCsvRecord(final String rank, final String level, final String experience) {
        CSVRecord record = mock(CSVRecord.class);
        Mockito.lenient().when(record.get(0)).thenReturn(rank);
        Mockito.lenient().when(record.get(1)).thenReturn(level);
        Mockito.lenient().when(record.get(2)).thenReturn(experience);

        return record;
    }
}