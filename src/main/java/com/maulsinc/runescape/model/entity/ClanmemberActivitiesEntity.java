package com.maulsinc.runescape.model.entity;

import com.maulsinc.runescape.model.Activity;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ClanmemberActivitiesEntity {
    @Id
    private ObjectId id;

    private String clanmember;
    private List<Activity> activities;
}
