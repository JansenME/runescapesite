package com.runescape.info.repository;

import com.runescape.info.model.entity.ClanmemberLevelsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmemberLevelsRepository extends MongoRepository<ClanmemberLevelsEntity, Long> {
    ClanmemberLevelsEntity findFirstByClanmemberOrderByIdDesc(String clanmember);
}