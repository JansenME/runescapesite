package com.runescape.info.repository;

import com.runescape.info.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Item, Long> {
}
