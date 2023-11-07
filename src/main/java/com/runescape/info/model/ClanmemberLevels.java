package com.runescape.info.model;

import com.runescape.info.model.entity.ClanmemberLevelsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanmemberLevels {
    private String clanmember;
    private List<Level> levels;

    public static ClanmemberLevels mapEntityToModel(final ClanmemberLevelsEntity clanmemberLevelsEntity) {
        return new ClanmemberLevels(clanmemberLevelsEntity.getClanmember(), clanmemberLevelsEntity.getLevels());
    }
}
