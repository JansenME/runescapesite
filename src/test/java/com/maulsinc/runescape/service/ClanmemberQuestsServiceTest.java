package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maulsinc.runescape.model.ClanmemberQuests;
import com.maulsinc.runescape.model.Quest;
import com.maulsinc.runescape.model.QuestDifficulty;
import com.maulsinc.runescape.model.QuestStatus;
import com.maulsinc.runescape.model.entity.ClanmemberQuestsEntity;
import com.maulsinc.runescape.repository.ClanmemberQuestsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
class ClanmemberQuestsServiceTest {
    private static final String NAME = "HC_Kloeperd";
    static ObjectMapper mapper;

    @Mock ClanmemberQuestsRepository clanmemberQuestsRepository;

    @InjectMocks ClanmemberQuestsService clanmemberQuestsService;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void testGetOneClanmemberQuestsHappyFlow() {
        when(clanmemberQuestsRepository.findFirstByClanmemberOrderByIdDesc(anyString())).thenReturn(createClanmemberQuestsEntity());

        ClanmemberQuests clanmemberQuests = clanmemberQuestsService.getOneClanmemberQuests(NAME);

        assertEquals(NAME, clanmemberQuests.getClanmember());
        assertEquals(250, clanmemberQuests.getTotalQuestPoints());

        assertEquals("Quest 1", clanmemberQuests.getQuests().get(0).getTitle());
        assertEquals(QuestStatus.NOT_STARTED, clanmemberQuests.getQuests().get(0).getStatus());
        assertEquals(QuestDifficulty.EXPERIENCED, clanmemberQuests.getQuests().get(0).getDifficulty());
        assertTrue(clanmemberQuests.getQuests().get(0).isMembers());
        assertEquals(2, clanmemberQuests.getQuests().get(0).getQuestPoints());
        assertTrue(clanmemberQuests.getQuests().get(0).isUserEligible());

        assertEquals("Quest 2", clanmemberQuests.getQuests().get(1).getTitle());
        assertEquals(QuestStatus.STARTED, clanmemberQuests.getQuests().get(1).getStatus());
        assertEquals(QuestDifficulty.EXPERIENCED, clanmemberQuests.getQuests().get(1).getDifficulty());
        assertTrue(clanmemberQuests.getQuests().get(1).isMembers());
        assertEquals(3, clanmemberQuests.getQuests().get(1).getQuestPoints());
        assertTrue(clanmemberQuests.getQuests().get(1).isUserEligible());

        assertEquals("Quest 3", clanmemberQuests.getQuests().get(2).getTitle());
        assertEquals(QuestStatus.STARTED, clanmemberQuests.getQuests().get(2).getStatus());
        assertEquals(QuestDifficulty.MASTER, clanmemberQuests.getQuests().get(2).getDifficulty());
        assertFalse(clanmemberQuests.getQuests().get(2).isMembers());
        assertEquals(1, clanmemberQuests.getQuests().get(2).getQuestPoints());
        assertTrue(clanmemberQuests.getQuests().get(2).isUserEligible());

        assertEquals("Quest 4", clanmemberQuests.getQuests().get(3).getTitle());
        assertEquals(QuestStatus.COMPLETED, clanmemberQuests.getQuests().get(3).getStatus());
        assertEquals(QuestDifficulty.EXPERIENCED, clanmemberQuests.getQuests().get(3).getDifficulty());
        assertTrue(clanmemberQuests.getQuests().get(3).isMembers());
        assertEquals(2, clanmemberQuests.getQuests().get(3).getQuestPoints());
        assertFalse(clanmemberQuests.getQuests().get(3).isUserEligible());
    }

    @Test
    void testGetOneClanmemberQuestsEntityNull() {
        ClanmemberQuests clanmemberQuests = clanmemberQuestsService.getOneClanmemberQuests("NAME_NOT_IN_DATABASE");

        assertNull(clanmemberQuests.getClanmember());
        assertTrue(clanmemberQuests.getQuests().isEmpty());
        assertNull(clanmemberQuests.getTotalQuestPoints());
        assertNull(clanmemberQuests.getDate());
    }

    @Test
    void testSaveClanmemberQuestsToDatabaseHappyFlow() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode
                .put("title", "Quest 1")
                .put("status", "STARTED")
                .put("difficulty", "3")
                .put("members", "true")
                .put("questPoints", "2")
                .put("userEligible", "true");

        ArrayNode arrayNode = mapper.createArrayNode().add(objectNode);

        clanmemberQuestsService.saveClanmemberQuestsToDatabase(NAME, arrayNode);

        verify(clanmemberQuestsRepository, times(1)).save(any());
    }

    @Test
    void testSaveClanmemberQuestsToDatabaseNullClanmember() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode
                .put("title", "Quest 1")
                .put("status", "STARTED")
                .put("difficulty", "3")
                .put("members", "true")
                .put("questPoints", "2")
                .put("userEligible", "true");

        clanmemberQuestsService.saveClanmemberQuestsToDatabase(null, objectNode);

        verify(clanmemberQuestsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberQuestsToDatabaseNullJsonNode() {
        clanmemberQuestsService.saveClanmemberQuestsToDatabase(NAME, null);

        verify(clanmemberQuestsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberQuestsToDatabaseEmptyJsonNode() {
        clanmemberQuestsService.saveClanmemberQuestsToDatabase(NAME, mapper.createObjectNode());

        verify(clanmemberQuestsRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberQuestsToDatabaseNullNode() {
        clanmemberQuestsService.saveClanmemberQuestsToDatabase(NAME, mapper.nullNode());

        verify(clanmemberQuestsRepository, times(0)).save(any());
    }

    @Test
    void testGetClanmemberQuestsEntityHappyFlow() {
        ObjectNode quest1 = mapper.createObjectNode();

        quest1
                .put("title", "Quest 1")
                .put("status", "STARTED")
                .put("difficulty", "3")
                .put("members", "true")
                .put("questPoints", "2")
                .put("userEligible", "true");

        ObjectNode quest2 = mapper.createObjectNode();

        quest2
                .put("title", "Quest 2")
                .put("status", "COMPLETED")
                .put("difficulty", "3")
                .put("members", "true")
                .put("questPoints", "2")
                .put("userEligible", "true");

        ObjectNode quest3 = mapper.createObjectNode();

        quest3
                .put("title", "Quest 3")
                .put("status", "COMPLETED")
                .put("difficulty", "4")
                .put("members", "true")
                .put("questPoints", "3")
                .put("userEligible", "true");

        ArrayNode arrayNode = mapper.createArrayNode().add(quest1).add(quest2).add(quest3);

        ClanmemberQuestsEntity clanmemberQuestsEntity = clanmemberQuestsService.getClanmemberQuestsEntity(NAME, arrayNode);

        assertEquals(NAME, clanmemberQuestsEntity.getClanmember());
        assertEquals(3, clanmemberQuestsEntity.getQuests().size());

        assertEquals(QuestDifficulty.MASTER, clanmemberQuestsEntity.getQuests().get(0).getDifficulty());
        assertEquals("Quest 1", clanmemberQuestsEntity.getQuests().get(0).getTitle());
        assertEquals(QuestDifficulty.GRANDMASTER, clanmemberQuestsEntity.getQuests().get(2).getDifficulty());

        assertEquals(5, clanmemberQuestsEntity.getTotalQuestPoints());
    }

    private ClanmemberQuestsEntity createClanmemberQuestsEntity() {
        ClanmemberQuestsEntity clanmemberQuestsEntity = new ClanmemberQuestsEntity();

        clanmemberQuestsEntity.setId(ObjectId.get());
        clanmemberQuestsEntity.setClanmember(NAME);
        clanmemberQuestsEntity.setQuests(createQuestsList());
        clanmemberQuestsEntity.setTotalQuestPoints(250);

        return clanmemberQuestsEntity;
    }

    private List<Quest> createQuestsList() {
        List<Quest> quests = new ArrayList<>();

        quests.add(createQuest("Quest 1", QuestStatus.NOT_STARTED, QuestDifficulty.EXPERIENCED, true, 2, true));
        quests.add(createQuest("Quest 2", QuestStatus.STARTED, QuestDifficulty.EXPERIENCED, true, 3, true));
        quests.add(createQuest("Quest 3", QuestStatus.STARTED, QuestDifficulty.MASTER, false, 1, true));
        quests.add(createQuest("Quest 4", QuestStatus.COMPLETED, QuestDifficulty.EXPERIENCED, true, 2, false));

        return quests;
    }

    private Quest createQuest(final String title, final QuestStatus status, final QuestDifficulty difficulty, final boolean members, final Integer questPoints, final boolean userEligible) {
        Quest quest = new Quest();

        quest.setTitle(title);
        quest.setStatus(status);
        quest.setDifficulty(difficulty);
        quest.setMembers(members);
        quest.setQuestPoints(questPoints);
        quest.setUserEligible(userEligible);

        return quest;
    }
}