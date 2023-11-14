package com.maulsinc.runescape.repository;

import com.maulsinc.runescape.model.entity.ClanmembersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmembersRepository extends MongoRepository<ClanmembersEntity, Long> {
    ClanmembersEntity findFirstByOrderByIdDesc();
}
