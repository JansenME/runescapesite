package com.runescape.info.repository;

import com.runescape.info.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
public interface ItemsRepository extends JpaRepository<Items, Long> {
    void findAllBySkill(String skill);
}
