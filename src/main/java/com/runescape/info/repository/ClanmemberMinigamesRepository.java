package com.runescape.info.repository;

import com.runescape.info.model.entity.ClanmemberMinigamesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmemberMinigamesRepository extends MongoRepository <ClanmemberMinigamesEntity, Long> {
    ClanmemberMinigamesEntity findFirstByClanmemberOrderByIdDesc(String clanmember);
}
