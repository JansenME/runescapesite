package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberQuestsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.maulsinc.runescape.CommonsService.getDateAsString;

@Data
@AllArgsConstructor
public class ClanmemberQuests {
    private String clanmember;
    private List<Quest> quests;
    private Integer totalQuestPoints;
    private String date;

    public ClanmemberQuests() {
        this.quests = new ArrayList<>();
    }

    public String getTotalQuestPointsAsString() {
        if(totalQuestPoints == null || totalQuestPoints == -1 || totalQuestPoints == 0) {
            return "--";
        }
        return String.valueOf(totalQuestPoints);
    }

    public static ClanmemberQuests mapEntityToModel(final ClanmemberQuestsEntity clanmemberQuestsEntity) {
        List<Quest> questsFromEntity = clanmemberQuestsEntity.getQuests();
        questsFromEntity.sort(Comparator.comparing(Quest::getTitle));

        return new ClanmemberQuests(clanmemberQuestsEntity.getClanmember(), questsFromEntity, clanmemberQuestsEntity.getTotalQuestPoints(), getDateAsString(clanmemberQuestsEntity.getId().getDate()));
    }
}
