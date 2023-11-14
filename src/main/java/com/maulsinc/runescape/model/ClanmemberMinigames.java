package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanmemberMinigames {
    private String clanmember;
    private List<Minigame> minigames;
    private String date;

    public static ClanmemberMinigames mapEntityToModel(final ClanmemberMinigamesEntity clanmemberMinigamesEntity, final String date) {
        return new ClanmemberMinigames(clanmemberMinigamesEntity.getClanmember(), clanmemberMinigamesEntity.getMinigames(), date);
    }
}
