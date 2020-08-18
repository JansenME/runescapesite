package com.runescape.info.model;

import com.runescape.info.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemsList {
    private List<Item> items;
}
