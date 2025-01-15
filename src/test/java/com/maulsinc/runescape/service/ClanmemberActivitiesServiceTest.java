package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maulsinc.runescape.model.Activity;
import com.maulsinc.runescape.model.ClanmemberActivities;
import com.maulsinc.runescape.model.entity.ClanmemberActivitiesEntity;
import com.maulsinc.runescape.repository.ClanmemberActivitiesRepository;
import org.bson.types.ObjectId;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.N;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.attribute.standard.MediaSize;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClanmemberActivitiesServiceTest {
    private static final String NAME = "HC_Kloeperd";
    static ObjectMapper mapper;

    @InjectMocks ClanmemberActivitiesService clanmemberActivitiesService;

    @Mock ClanmemberActivitiesRepository clanmemberActivitiesRepository;

    @BeforeAll
    static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void testGetOneClanmemberActivitiesHappyFlow() {
        when(clanmemberActivitiesRepository.findFirstByClanmemberOrderByIdDesc(anyString())).thenReturn(createClanmemberActivitiesEntity());

        ClanmemberActivities clanmemberActivities = clanmemberActivitiesService.getOneClanmemberActivities(NAME);

        assertEquals(NAME, clanmemberActivities.getClanmember());

        assertEquals("14-Jan-2025 14:00", clanmemberActivities.getActivities().get(0).getDate());
        assertEquals("I have done something important", clanmemberActivities.getActivities().get(0).getDetails());
        assertEquals("something important", clanmemberActivities.getActivities().get(0).getText());

        assertEquals("14-Jan-2025 15:00", clanmemberActivities.getActivities().get(1).getDate());
        assertEquals("I have done something important again", clanmemberActivities.getActivities().get(1).getDetails());
        assertEquals("something important again", clanmemberActivities.getActivities().get(1).getText());

        assertEquals("14-Jan-2025 16:00", clanmemberActivities.getActivities().get(2).getDate());
        assertEquals("I have done something important again again", clanmemberActivities.getActivities().get(2).getDetails());
        assertEquals("something important again again", clanmemberActivities.getActivities().get(2).getText());

        assertEquals("14-Jan-2025 17:00", clanmemberActivities.getActivities().get(3).getDate());
        assertEquals("I have done something important again again again", clanmemberActivities.getActivities().get(3).getDetails());
        assertEquals("something important again again again", clanmemberActivities.getActivities().get(3).getText());
    }

    @Test
    void testGetOneClanmemberActivitiesEntityNull() {
        ClanmemberActivities clanmemberActivities = clanmemberActivitiesService.getOneClanmemberActivities("NAME_NOT_IN_DATABASE");

        assertNull(clanmemberActivities.getClanmember());
        assertNull(clanmemberActivities.getDate());
        assertTrue(clanmemberActivities.getActivities().isEmpty());
    }

    @Test
    void testSaveClanmemberActivitiesToDatabaseHappyFlow() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode
                .put("date", "")
                .put("details","")
                .put("text", "");

        ArrayNode arrayNode = mapper.createArrayNode().add(objectNode);

        clanmemberActivitiesService.saveClanmemberActivitiesToDatabase(NAME, arrayNode);

        verify(clanmemberActivitiesRepository, times(1)).save(any());
    }

    @Test
    void testSaveClanmemberActivitiesToDatabaseNullClanmember() {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode
                .put("date", "")
                .put("details","")
                .put("text", "");

        ArrayNode arrayNode = mapper.createArrayNode().add(objectNode);

        clanmemberActivitiesService.saveClanmemberActivitiesToDatabase(null, arrayNode);

        verify(clanmemberActivitiesRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberActivitiesToDatabaseActivityNullJsonNode() {
        clanmemberActivitiesService.saveClanmemberActivitiesToDatabase(NAME, null);

        verify(clanmemberActivitiesRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberActivitiesToDatabaseActivityEmptyJsonNode() {
        clanmemberActivitiesService.saveClanmemberActivitiesToDatabase(NAME, mapper.createObjectNode());

        verify(clanmemberActivitiesRepository, times(0)).save(any());
    }

    @Test
    void testSaveClanmemberActivitiesToDatabaseActivityNullNode() {
        clanmemberActivitiesService.saveClanmemberActivitiesToDatabase(NAME, mapper.nullNode());

        verify(clanmemberActivitiesRepository, times(0)).save(any());
    }

    @Test
    void testGetClanmemberActivitiesEntityHappyFlow() {
        ObjectNode activity1 = mapper.createObjectNode();

        activity1
                .put("date", "14-Jan-2025 14:00")
                .put("details", "I have done something important")
                .put("text", "something important");

        ObjectNode activity2 = mapper.createObjectNode();

        activity2
                .put("date", "14-Jan-2025 15:00")
                .put("details", "I have done something important again")
                .put("text", "something important again");

        ObjectNode activity3 = mapper.createObjectNode();

        activity3
                .put("date", "14-Jan-2025 16:00")
                .put("details", "I have done something important again again")
                .put("text", "something important again again");

        ObjectNode activity4 = mapper.createObjectNode();

        activity4
                .put("date", "14-Jan-2025 17:00")
                .put("details", "I have done something important again again again")
                .put("text", "something important again again again");

        ArrayNode arrayNode = mapper.createArrayNode().add(activity1).add(activity2).add(activity3).add(activity4);

        ClanmemberActivitiesEntity clanmemberActivitiesEntity = clanmemberActivitiesService.getClanmemberActivitiesEntity(NAME, arrayNode);

        assertEquals(NAME, clanmemberActivitiesEntity.getClanmember());
        assertEquals(4, clanmemberActivitiesEntity.getActivities().size());

        assertEquals("14-Jan-2025 14:00", clanmemberActivitiesEntity.getActivities().get(0).getDate());
        assertEquals("I have done something important", clanmemberActivitiesEntity.getActivities().get(0).getDetails());
        assertEquals("something important", clanmemberActivitiesEntity.getActivities().get(0).getText());

        assertEquals("14-Jan-2025 15:00", clanmemberActivitiesEntity.getActivities().get(1).getDate());
        assertEquals("I have done something important again", clanmemberActivitiesEntity.getActivities().get(1).getDetails());
        assertEquals("something important again", clanmemberActivitiesEntity.getActivities().get(1).getText());

        assertEquals("14-Jan-2025 16:00", clanmemberActivitiesEntity.getActivities().get(2).getDate());
        assertEquals("I have done something important again again", clanmemberActivitiesEntity.getActivities().get(2).getDetails());
        assertEquals("something important again again", clanmemberActivitiesEntity.getActivities().get(2).getText());

        assertEquals("14-Jan-2025 17:00", clanmemberActivitiesEntity.getActivities().get(3).getDate());
        assertEquals("I have done something important again again again", clanmemberActivitiesEntity.getActivities().get(3).getDetails());
        assertEquals("something important again again again", clanmemberActivitiesEntity.getActivities().get(3).getText());
    }

    private ClanmemberActivitiesEntity createClanmemberActivitiesEntity() {
        ClanmemberActivitiesEntity clanmemberActivitiesEntity = new ClanmemberActivitiesEntity();

        clanmemberActivitiesEntity.setId(ObjectId.get());
        clanmemberActivitiesEntity.setClanmember(NAME);
        clanmemberActivitiesEntity.setActivities(createActivityList());

        return clanmemberActivitiesEntity;
    }

    private List<Activity> createActivityList() {
        List<Activity> activities = new ArrayList<>();

        activities.add(createActivity("14-Jan-2025 14:00", "I have done something important", "something important"));
        activities.add(createActivity("14-Jan-2025 15:00", "I have done something important again", "something important again"));
        activities.add(createActivity("14-Jan-2025 16:00", "I have done something important again again", "something important again again"));
        activities.add(createActivity("14-Jan-2025 17:00", "I have done something important again again again", "something important again again again"));

        return activities;
    }

    private Activity createActivity(String date, String details, String text) {
        Activity activity = new Activity();

        activity.setDate(date);
        activity.setDetails(details);
        activity.setText(text);

        return activity;
    }
}