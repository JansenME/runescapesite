package com.runescape.info;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ItemPrice {
    private static final Logger logger = LoggerFactory.getLogger(ItemPrice.class);

    private ItemPrice() {
    }

    public static BigDecimal getItemPrice(URL link, Long itemId) {
        logger.info("I'm going to try and get the item price for item with id {}", itemId);
        BufferedReader br;
        URLConnection conn;

        try {
            conn = link.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            logger.info("I succesfully got a stream for you.");
        } catch (Exception e) {
            logger.error("I failed getting a stream.", e);
            return BigDecimal.valueOf(0);
        }

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(br);

        JsonObject jsonObject = (JsonObject) root;

        Map<String, Object> flattenedJsonMap = JsonFlattener.flattenAsMap(jsonObject.toString());
        SortedSet<String> keys = new TreeSet<>(flattenedJsonMap.keySet());
        logger.info("I succesfully got a price ({})", flattenedJsonMap.get(keys.last()));
        return (BigDecimal) flattenedJsonMap.get(keys.last());
    }
}
