package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.ClanmemberQuests;
import com.maulsinc.runescape.model.Quest;
import com.maulsinc.runescape.model.QuestDifficulty;
import com.maulsinc.runescape.model.QuestStatus;
import com.maulsinc.runescape.model.entity.ClanmemberQuestsEntity;
import com.maulsinc.runescape.repository.ClanmemberQuestsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ClanmemberQuestsService {
    private final ClanmemberQuestsRepository clanmemberQuestsRepository;

    @Autowired
    public ClanmemberQuestsService(final ClanmemberQuestsRepository clanmemberQuestsRepository) {
        this.clanmemberQuestsRepository = clanmemberQuestsRepository;
    }

    @ExecutionTimeLogger
    public ClanmemberQuests getOneClanmemberQuests(final String clanmember) {
        ClanmemberQuestsEntity clanmemberQuestsEntity = clanmemberQuestsRepository.findFirstByClanmemberOrderByIdDesc(clanmember);

        if(clanmemberQuestsEntity == null) {
            return new ClanmemberQuests();
        }

        return ClanmemberQuests.mapEntityToModel(clanmemberQuestsEntity);
    }

    public void saveClanmemberQuestsToDatabase(final String clanmember, final JsonNode jsonNodeQuests) {
        ClanmemberQuestsEntity clanmemberQuestsEntity = getClanmemberQuestsEntity(clanmember, jsonNodeQuests);

        if(!CollectionUtils.isEmpty(clanmemberQuestsEntity.getQuests())) {
            clanmemberQuestsRepository.save(clanmemberQuestsEntity);
        }
    }

    ClanmemberQuestsEntity getClanmemberQuestsEntity(final String clanmember, final JsonNode jsonNodeQuests) {
        ClanmemberQuestsEntity clanmemberQuestsEntity = new ClanmemberQuestsEntity();

        List<Quest> quests = getWholeQuestList(jsonNodeQuests);

        clanmemberQuestsEntity.setClanmember(clanmember);
        clanmemberQuestsEntity.setQuests(quests);
        clanmemberQuestsEntity.setTotalQuestPoints(setTotalQuestPoints(quests));

        return clanmemberQuestsEntity;
    }

    private List<Quest> getWholeQuestList(final JsonNode jsonNodeQuests) {
        List<Quest> quests = new ArrayList<>();

        if(jsonNodeQuests != null && jsonNodeQuests.isArray()) {
            for(JsonNode jsonNode : jsonNodeQuests) {
                quests.add(mapJsonNodeToQuest(jsonNode));
            }
        }

        return quests;
    }

    private Quest mapJsonNodeToQuest(final JsonNode jsonNode) {
        Quest quest = new Quest();

        quest.setTitle(jsonNode.get("title").asText());
        quest.setStatus(QuestStatus.getEnumByText(jsonNode.get("status").asText()));
        quest.setDifficulty(QuestDifficulty.getQuestDifficultyByNumber(jsonNode.get("difficulty").asInt()));
        quest.setMembers(jsonNode.get("members").asBoolean());
        quest.setQuestPoints(jsonNode.get("questPoints").asInt());
        quest.setUserEligible(jsonNode.get("userEligible").asBoolean());

        return quest;
    }

    private Integer setTotalQuestPoints(List<Quest> quests) {
        AtomicInteger totalQuestPoints = new AtomicInteger(0);

        quests.forEach(quest -> {
            if(QuestStatus.COMPLETED.equals(quest.getStatus())) {
                totalQuestPoints.set(totalQuestPoints.get() + quest.getQuestPoints());
            }
        });

        return totalQuestPoints.get();
    }
}
