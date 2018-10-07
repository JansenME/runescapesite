package com.runescape.info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
@Controller
public class MainController {
    @RequestMapping(value = {"/", "/index", "/home"}, method = RequestMethod.GET)
    public String goToIndex() {
        return "index";
    }
}
