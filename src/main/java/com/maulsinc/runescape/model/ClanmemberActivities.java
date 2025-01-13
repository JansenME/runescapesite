package com.maulsinc.runescape.model;

import com.maulsinc.runescape.model.entity.ClanmemberActivitiesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.maulsinc.runescape.CommonsService.getDateAsString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClanmemberActivities {
    private String clanmember;
    private List<Activity> activities;
    private String date;

    public static ClanmemberActivities mapEntityModel(final ClanmemberActivitiesEntity clanmemberActivitiesEntity) {
        return new ClanmemberActivities(clanmemberActivitiesEntity.getClanmember(), clanmemberActivitiesEntity.getActivities(), getDateAsString(clanmemberActivitiesEntity.getId().getDate()));
    }
}
