package com.runescape.info.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
@Controller
public class CalculatorsController {

    @RequestMapping(value = "/calculators", method = RequestMethod.POST)
    public String goToCalculators() {
        return "calculators";
    }

    @RequestMapping(value = "/calculators", method = RequestMethod.GET)
    public String getCalculators() {
        return "calculators";
    }
}
