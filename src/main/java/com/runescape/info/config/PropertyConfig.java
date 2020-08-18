package com.runescape.info.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyConfig {
    @Value("${url.item.info}")
    private String urlItemInfo;

    @Value("${url.item.price}")
    private String urlItemPrice;

    public String getUrlItemInfo(Long itemId) {
        return this.urlItemInfo + itemId;
    }

    public String getUrlItemPrice(Long itemId) {
        return this.urlItemPrice + itemId + ".json";
    }
}
