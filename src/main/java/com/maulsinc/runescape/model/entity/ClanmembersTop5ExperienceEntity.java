package com.maulsinc.runescape.model.entity;

import com.maulsinc.runescape.model.ClanmemberLevels;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ClanmembersTop5ExperienceEntity {
    @Id
    private ObjectId id;

    private List<ClanmemberLevels> clanmemberLevels;
}
