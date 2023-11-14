package com.maulsinc.runescape.repository;

import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmemberMinigamesRepository extends MongoRepository <ClanmemberMinigamesEntity, Long> {
    ClanmemberMinigamesEntity findFirstByClanmemberOrderByIdDesc(String clanmember);
}
