package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberActivitiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanmemberActivities {
    private String clanmember;
    private List<Activity> activities;

    public static ClanmemberActivities mapEntityModel(final ClanmemberActivitiesEntity clanmemberActivitiesEntity) {
        return new ClanmemberActivities(clanmemberActivitiesEntity.getClanmember(), clanmemberActivitiesEntity.getActivities());
    }
}
