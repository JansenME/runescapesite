package com.runescape.info.controller;

import com.runescape.info.service.MemberInfoService;
import com.sun.syndication.io.FeedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberInfoController {
    private static final Logger logger = LoggerFactory.getLogger(MemberInfoController.class);

    @RequestMapping(value = "/member/{name}", method = RequestMethod.GET)
    public ModelAndView getMemberLevels(@PathVariable String name) throws FeedException {
        logger.info("In method getMemberLevels...");

        MemberInfoService memberInfoService = new MemberInfoService();

        return memberInfoService.getMemberPageInfo(name);
    }
}
