package com.runescape.info.repository;

import com.runescape.info.entity.PlayerLevels;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerLevelsRepository extends MongoRepository<PlayerLevels, Long> {
}
