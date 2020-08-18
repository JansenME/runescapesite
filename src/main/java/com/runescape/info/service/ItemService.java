package com.runescape.info.service;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.runescape.info.config.PropertyConfig;
import com.runescape.info.entity.Item;
import com.runescape.info.exception.RunescapeSiteException;
import com.runescape.info.model.ItemInfo;
import com.runescape.info.model.ItemsList;
import com.runescape.info.repository.ItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {
    private static final String POSITIVE = "positive";

    private final ItemsRepository itemsRepository;
    private final PropertyConfig propertyConfig;

    @Autowired
    public ItemService(ItemsRepository itemsRepository, PropertyConfig propertyConfig) {
        this.itemsRepository = itemsRepository;
        this.propertyConfig = propertyConfig;
    }

    public ItemsList getAllItems() {
        List<Item> items = itemsRepository.findAll();

        //Sort alphabetically on name
        items.sort((item1, item2) -> item1.getNameItem().compareToIgnoreCase(item2.getNameItem()));

        return new ItemsList(items);
    }

    public ItemInfo getOneItem(final Long ownItemId) throws RunescapeSiteException {
        Item item = itemsRepository.findOne(ownItemId);

        Long itemID = item.getRunescapeId();

        Map<String, String> itemsInfo;
        String formattedPrice;

        try {
            itemsInfo = getInfoItemService(itemID);
            formattedPrice = getFormattedPrice(itemID);
        } catch (MalformedURLException e) {
            String message = "Something was wrong with the URL provided.";
            log.error(message);
            throw new RunescapeSiteException(message);
        }

        if (itemsInfo == null || itemsInfo.isEmpty()) {
            throw new RunescapeSiteException("The call for itemsInfo failed.");
        }

        return ItemInfo.builder()
                .itemIconSmall(itemsInfo.get(ItemValues.ITEM_ICON_SMALL.name()))
                .itemDescription(itemsInfo.get(ItemValues.ITEM_DESCRIPTION.name()))
                .itemIconLarge(itemsInfo.get(ItemValues.ITEM_ICON_LARGE.name()))
                .itemName(itemsInfo.get(ItemValues.ITEM_NAME.name()))
                .itemType(itemsInfo.get(ItemValues.ITEM_TYPE.name()))
                .itemTodayTrend(valueIsPositive(itemsInfo.get(ItemValues.ITEM_TODAY_TREND.name())))
                .itemTodayPrice(itemsInfo.get(ItemValues.ITEM_TODAY_PRICE.name()))
                .itemMembers(itemsInfo.get(ItemValues.ITEM_MEMBERS.name()))
                .itemDay30Trend(valueIsPositive(itemsInfo.get(ItemValues.ITEM_DAY_30_TREND.name())))
                .itemDay30Change(itemsInfo.get(ItemValues.ITEM_DAY_30_CHANGE.name()))
                .itemDay90Trend(valueIsPositive(itemsInfo.get(ItemValues.ITEM_DAY_90_TREND.name())))
                .itemDay90Change(itemsInfo.get(ItemValues.ITEM_DAY_90_CHANGE.name()))
                .itemDay180Trend(valueIsPositive(itemsInfo.get(ItemValues.ITEM_DAY_180_TREND.name())))
                .itemDay180Change(itemsInfo.get(ItemValues.ITEM_DAY_180_CHANGE.name()))
                .itemPrice(formattedPrice)
                .itemExperience(getExperience(item))
                .itemLevelNeeded(item.getLevelNeeded())
                .itemSkill(item.getSkill())
                .build();
    }

    private boolean valueIsPositive(final String name) {
        return POSITIVE.equalsIgnoreCase(name);
    }

    private String getExperience(final Item item) {
        String experience = item.getExperience().toString();

        if (experience.contains(".0")) {
            experience = experience.substring(0, experience.length() - 2);
        }

        return experience;
    }

    private Map<String, String> getInfoItemService(final Long itemId) throws MalformedURLException {
        return setValuesFromJson(getJsonAsMap(new URL(propertyConfig.getUrlItemInfo(itemId))));
    }

    private Map<String, String> setValuesFromJson(final Map<String, Object> jsonAsMap) {
        return Arrays.stream(ItemValues.values())
                .collect(Collectors.toMap(ItemValues::name, e -> String.valueOf(jsonAsMap.get(e.getJsonKey()))));
    }

    private String getFormattedPrice(final Long itemId) throws RunescapeSiteException, MalformedURLException {
        Map<String, Object> map = getJsonAsMap(new URL(propertyConfig.getUrlItemPrice(itemId)));

        if (map == null) {
            throw new RunescapeSiteException("The call for itemsPrices failed.");
        }

        SortedSet<String> keys = new TreeSet<>(map.keySet());
        BigDecimal price = (BigDecimal) map.get(keys.last());

        return new DecimalFormat("#,###").format(price);
    }

    private Map<String, Object> getJsonAsMap(final URL url) {
        try {
            URLConnection connection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            JsonElement element = new JsonParser().parse(bufferedReader);

            String json = element.getAsJsonObject().toString();

            return JsonFlattener.flattenAsMap(json);
        } catch (IOException e) {
            log.error("An exception has been thrown while trying to connect to the Runescape API.", e);
        }

        return null;
    }

    private enum ItemValues {
        ITEM_ICON_SMALL("item.icon"),
        ITEM_DESCRIPTION("item.description"),
        ITEM_ICON_LARGE("item.icon_large"),
        ITEM_NAME("item.name"),
        ITEM_TYPE("item.type"),
        ITEM_TODAY_TREND("item.today.trend"),
        ITEM_TODAY_PRICE("item.today.price"),
        ITEM_MEMBERS("item.members"),
        ITEM_DAY_30_TREND("item.day30.trend"),
        ITEM_DAY_30_CHANGE("item.day30.change"),
        ITEM_DAY_90_TREND("item.day90.trend"),
        ITEM_DAY_90_CHANGE("item.day90.change"),
        ITEM_DAY_180_TREND("item.day180.trend"),
        ITEM_DAY_180_CHANGE("item.day180.change");

        private final String jsonKey;

        ItemValues(final String jsonKey) {
            this.jsonKey = jsonKey;
        }

        public String getJsonKey() {
            return this.jsonKey;
        }
    }
}
