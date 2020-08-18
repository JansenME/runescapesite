package com.runescape.info.controller;

import com.runescape.info.model.ItemInfo;
import com.runescape.info.model.ItemsList;
import com.runescape.info.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public ResponseEntity<ItemsList> itemList() {
        try {
            return ResponseEntity.ok(itemService.getAllItems());
        } catch (Exception e) {
            log.error("Exception thrown.", e);
        }

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @GetMapping("/item/{ownItemId}")
    public ResponseEntity<ItemInfo> showItem(@PathVariable Long ownItemId) {
        try{
            return ResponseEntity.ok(itemService.getOneItem(ownItemId));
        } catch (Exception e) {
            log.error("Exception thrown.", e);
        }

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
