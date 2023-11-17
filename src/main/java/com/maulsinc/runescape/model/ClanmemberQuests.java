package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberQuestsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClanmemberQuests {
    private String clanmember;
    private List<Quest> quests;
    private Integer totalQuestPoints;
    private String date;

    public static ClanmemberQuests mapEntityToModel(final ClanmemberQuestsEntity clanmemberQuestsEntity, final String date) {
        List<Quest> questsFromEntity = clanmemberQuestsEntity.getQuests();
        questsFromEntity.sort(Comparator.comparing(Quest::getTitle));

        return new ClanmemberQuests(clanmemberQuestsEntity.getClanmember(), questsFromEntity, clanmemberQuestsEntity.getTotalQuestPoints(), date);
    }
}
