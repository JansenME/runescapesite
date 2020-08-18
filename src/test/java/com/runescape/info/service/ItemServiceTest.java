package com.runescape.info.service;

import com.runescape.info.entity.Item;
import com.runescape.info.exception.RunescapeSiteException;
import com.runescape.info.model.ItemInfo;
import com.runescape.info.model.ItemsList;
import com.runescape.info.repository.ItemsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {
    private static final Long ID = 1L;
    private List<Item> items = new ArrayList<>();
    private Item item;

    @InjectMocks
    private ItemService itemServiceMock;

    @Mock
    private ItemsRepository itemsRepositoryMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        item = new Item();
        item.setId(ID);
        item.setRunescapeId(123L);
        item.setNameItem("Name");
        item.setExperience(new BigDecimal(50));
        item.setLevelNeeded(20);
        item.setSkill("Skill");
    }

    @Test
    public void getAllItems() {
        when(itemServiceMock.getAllItems().getItems()).thenReturn(items);

        ItemsList list = itemServiceMock.getAllItems();
        assertEquals(0, list.getItems().size());

        items.add(item);
        list = itemServiceMock.getAllItems();
        assertEquals(1, list.getItems().size());
    }

    @Test
    public void getOneItem() throws RunescapeSiteException {
        when(itemsRepositoryMock.findOne(ID)).thenReturn(item);

        ItemInfo itemInfo = itemServiceMock.getOneItem(ID);

        System.out.println(itemInfo);
    }
}