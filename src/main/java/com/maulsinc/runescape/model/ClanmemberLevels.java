package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberLevelsEntity;
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
    private String date;

    public static ClanmemberLevels mapEntityToModel(final ClanmemberLevelsEntity clanmemberLevelsEntity, final String date) {
        return new ClanmemberLevels(clanmemberLevelsEntity.getClanmember(), clanmemberLevelsEntity.getLevels(), date);
    }
}
