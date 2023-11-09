package com.runescape.info.model.entity;

import com.runescape.info.model.Minigame;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ClanmemberMinigamesEntity {
    @Id
    private ObjectId id;

    private String clanmember;
    private List<Minigame> minigames;
}
