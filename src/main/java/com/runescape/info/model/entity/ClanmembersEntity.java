package com.runescape.info.model.entity;

import com.runescape.info.model.Clanmember;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClanmembersEntity {
    @Id
    private ObjectId id;

    private List<Clanmember> clanmembers;

    public ClanmembersEntity(final List<Clanmember> clanmembers) {
        this.clanmembers = clanmembers;
    }
}
