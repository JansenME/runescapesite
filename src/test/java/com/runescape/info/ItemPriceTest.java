package com.runescape.info;

import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ItemPriceTest {
    @Test
    public void getItemPriceTest() {
        URL link = getClass().getClassLoader().getResource("itemPriceTest.json");

        BigDecimal price = ItemPrice.getItemPrice(link, 0L);

        assertEquals(500, price.intValue());
    }

    @Test
    public void getItemPriceTestFileNotFound() {
        URL link = getClass().getClassLoader().getResource("itemPriceTestNotHere.json");
        BigDecimal price = ItemPrice.getItemPrice(link, 0L);

        assertEquals(0, price.intValue());
    }
}
