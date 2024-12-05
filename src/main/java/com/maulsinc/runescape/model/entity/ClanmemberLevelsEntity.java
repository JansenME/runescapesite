package com.maulsinc.runescape.model.entity;

import com.maulsinc.runescape.model.Level;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ClanmemberLevelsEntity {
    @Id
    private ObjectId id;

    private String clanmember;
    private List<Level> levels;
}
