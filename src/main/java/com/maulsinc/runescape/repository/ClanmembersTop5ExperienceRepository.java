package com.maulsinc.runescape.repository;

import com.maulsinc.runescape.model.entity.ClanmembersTop5ExperienceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanmembersTop5ExperienceRepository extends MongoRepository<ClanmembersTop5ExperienceEntity, Long> {
    ClanmembersTop5ExperienceEntity findFirstByOrderByIdDesc();
}
