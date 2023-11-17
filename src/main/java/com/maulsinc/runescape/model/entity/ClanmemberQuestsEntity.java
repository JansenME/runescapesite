package com.maulsinc.runescape.model.entity;

import com.maulsinc.runescape.model.Quest;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ClanmemberQuestsEntity {
    @Id
    private ObjectId id;

    private String clanmember;
    private List<Quest> quests;
    private Integer totalQuestPoints;
}
