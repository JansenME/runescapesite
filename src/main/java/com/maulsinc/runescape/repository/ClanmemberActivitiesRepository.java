package com.maulsinc.runescape.repository;

import com.maulsinc.runescape.model.entity.ClanmemberActivitiesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmemberActivitiesRepository extends MongoRepository<ClanmemberActivitiesEntity, Long> {
    ClanmemberActivitiesEntity findFirstByClanmemberOrderByIdDesc(String clanmember);
}
