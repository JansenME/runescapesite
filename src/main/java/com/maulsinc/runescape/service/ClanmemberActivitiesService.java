package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.Activity;
import com.maulsinc.runescape.model.ClanmemberActivities;
import com.maulsinc.runescape.model.entity.ClanmemberActivitiesEntity;
import com.maulsinc.runescape.repository.ClanmemberActivitiesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClanmemberActivitiesService {
    private final ClanmemberActivitiesRepository clanmemberActivitiesRepository;

    @Autowired
    public ClanmemberActivitiesService(final ClanmemberActivitiesRepository clanmemberActivitiesRepository) {
        this.clanmemberActivitiesRepository = clanmemberActivitiesRepository;
    }

    @ExecutionTimeLogger
    public ClanmemberActivities getOneClanmemberActivities(final String clanmember) {
        ClanmemberActivitiesEntity clanmemberActivitiesEntity = clanmemberActivitiesRepository.findFirstByClanmemberOrderByIdDesc(clanmember);

        if(clanmemberActivitiesEntity == null) {
            return new ClanmemberActivities();
        }

        return ClanmemberActivities.mapEntityModel(clanmemberActivitiesEntity);
    }

    public void saveClanmemberActivitiesToDatabase(final String clanmember, final JsonNode jsonNodeActivities) {
        ClanmemberActivitiesEntity clanmemberActivitiesEntity = getClanmemberActivitiesEntity(clanmember, jsonNodeActivities);

        if(clanmember != null && !CollectionUtils.isEmpty(clanmemberActivitiesEntity.getActivities())) {
            clanmemberActivitiesRepository.save(clanmemberActivitiesEntity);
        }
    }

    ClanmemberActivitiesEntity getClanmemberActivitiesEntity(String clanmember, JsonNode jsonNodeActivities) {
        ClanmemberActivitiesEntity clanmemberActivitiesEntity = new ClanmemberActivitiesEntity();

        clanmemberActivitiesEntity.setClanmember(clanmember);
        clanmemberActivitiesEntity.setActivities(getActivities(jsonNodeActivities));

        return clanmemberActivitiesEntity;
    }

    private List<Activity> getActivities(final JsonNode jsonNodeActivities) {
        List<Activity> activities = new ArrayList<>();

        if(jsonNodeActivities != null && jsonNodeActivities.isArray()) {
            for(JsonNode activity : jsonNodeActivities) {
                activities.add(mapJsonNodeToActivity(activity));
            }
        }

        return activities;
    }

    private Activity mapJsonNodeToActivity(final JsonNode jsonNode) {
        Activity activity = new Activity();

        activity.setDate(jsonNode.get("date").asText());
        activity.setDetails(formatNumbersInsideString(jsonNode.get("details").asText()));
        activity.setText(formatNumbersInsideString(jsonNode.get("text").asText()));

        return activity;
    }

    @ExecutionTimeLogger
    private String formatNumbersInsideString(final String value) {
        if(checkIfValueHasNumbers(value)) {
            return buildNewString(value);
        }

        return value;
    }

    private boolean checkIfValueHasNumbers(final String value) {
        return value.matches(".*\\d.*");
    }

    private String buildNewString(final String value) {
        return value.split("[0-9]")[0] + formatNumberInValue(value) + value.split("[0-9]")[value.split("[0-9]").length - 1];
    }

    private String formatNumberInValue(final String value) {
        return CommonsService.getFormattedNumber(Long.valueOf(getOnlyNumber(value)));
    }

    private String getOnlyNumber(final String value) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i < value.length() ; i++) {
            if(Character.isDigit(value.charAt(i))) {
                stringBuilder.append(value.charAt(i));
            }
        }

        return stringBuilder.toString();
    }

}
