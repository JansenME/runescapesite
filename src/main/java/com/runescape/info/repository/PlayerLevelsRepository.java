package com.runescape.info.repository;

import com.runescape.info.entity.PlayerLevels;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerLevelsRepository extends MongoRepository<PlayerLevels, Long> {
    List<PlayerLevels> findAllByPlayer(String player);

    PlayerLevels findFirstByPlayerOrderByDateDesc(String player);
}
