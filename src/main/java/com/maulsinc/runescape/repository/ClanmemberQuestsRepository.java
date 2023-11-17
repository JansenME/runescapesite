package com.maulsinc.runescape.repository;

import com.maulsinc.runescape.model.entity.ClanmemberQuestsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmemberQuestsRepository extends MongoRepository<ClanmemberQuestsEntity, Long> {
    ClanmemberQuestsEntity findFirstByClanmemberOrderByIdDesc(String clanmember);
}
