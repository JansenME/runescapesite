package com.runescape.info.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
@Controller
public class MembersListController {
    private static final Logger logger = LoggerFactory.getLogger(MembersListController.class);

    @RequestMapping(value = "/members", method = RequestMethod.POST)
    public String goToMembers(Model model) {
        model.addAttribute("nameMember");
        return "members";
    }

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String getMembers() {
        logger.info("In method getMembers...");

        return "members";
    }
}
