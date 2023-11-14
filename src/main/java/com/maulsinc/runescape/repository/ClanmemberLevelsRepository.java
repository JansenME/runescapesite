package com.maulsinc.runescape.repository;

import com.maulsinc.runescape.model.entity.ClanmemberLevelsEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClanmemberLevelsRepository extends MongoRepository<ClanmemberLevelsEntity, Long> {
    ClanmemberLevelsEntity findFirstByClanmemberOrderByIdDesc(String clanmember);

    @Query("{_id:{$gt: ?1, $lt: ?2}}")
    List<ClanmemberLevelsEntity> findByObjectIdsAndAllClanmemberLevelsByClanmember(String clanmember, ObjectId idMin, ObjectId idMax);

}