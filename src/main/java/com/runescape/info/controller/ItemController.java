package com.runescape.info.controller;

import com.runescape.info.model.Items;
import com.runescape.info.repository.ItemsRepository;
import com.runescape.info.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
@Controller
public class ItemController {
    @Autowired
    private ItemsRepository itemsRepository;

    private String itemName = null;

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public ModelAndView itemList() {
        List<Items> itemList = itemsRepository.findAll();

        ItemService itemService = new ItemService();
        return itemService.itemListService(itemList);
    }

    @RequestMapping(value = "/item/{ownItemId}", method = RequestMethod.GET)
    public ModelAndView showItem(@PathVariable Long ownItemId) throws IOException {
        Items item = itemsRepository.findOne(ownItemId);

        ItemService itemService = new ItemService();
        return itemService.showItemService(item);
    }

    @RequestMapping(value = "/createItem", method = RequestMethod.GET)
    public String createItem(Model model) {
        model.addAttribute("itemsForm", new Items());

        return "createItem";
    }

    @RequestMapping(value = "/createItem", method = RequestMethod.POST)
    public String createItem(@ModelAttribute("itemsForm") Items itemForm, BindingResult bindingResult, Model model) throws IOException {
        ItemService itemService = new ItemService();
        itemService.getInfoItemService(itemForm.getRunescapeId());

        itemForm.setNameItem(this.itemName);
        itemsRepository.save(itemForm);

        return "redirect:/createItem";
    }


}
