package com.runescape.info.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
@Controller
public class OwnErrorController implements ErrorController {
    @Override
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String getErrorPath() {
        return "/error";
    }
}
