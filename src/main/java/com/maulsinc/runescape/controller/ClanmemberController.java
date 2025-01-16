package com.maulsinc.runescape.controller;

import com.maulsinc.runescape.model.Clanmember;
import com.maulsinc.runescape.model.ClanmemberActivities;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.ClanmemberMinigames;
import com.maulsinc.runescape.model.ClanmemberQuests;
import com.maulsinc.runescape.service.ClanmemberActivitiesService;
import com.maulsinc.runescape.service.ClanmemberLevelsService;
import com.maulsinc.runescape.service.ClanmemberMinigamesService;
import com.maulsinc.runescape.service.ClanmemberQuestsService;
import com.maulsinc.runescape.service.ClanmembersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Calendar;

import static com.maulsinc.runescape.CommonsService.COOKIE_NAME;
import static com.maulsinc.runescape.CommonsService.getDateAsUSString;
import static com.maulsinc.runescape.CommonsService.replaceEmptySpace;
import static com.maulsinc.runescape.CommonsService.replacePlusToSpace;

@Slf4j
@Controller
public class ClanmemberController {
    @Value("${app.version:unknown}")
    String version;

    private final ClanmembersService clanmembersService;
    private final ClanmemberLevelsService clanmemberLevelsService;
    private final ClanmemberMinigamesService clanmemberMinigamesService;
    private final ClanmemberQuestsService clanmemberQuestsService;
    private final ClanmemberActivitiesService clanmemberActivitiesService;

    @Autowired
    public ClanmemberController(final ClanmembersService clanmembersService,
                                final ClanmemberLevelsService clanmemberLevelsService,
                                final ClanmemberMinigamesService clanmemberMinigamesService,
                                final ClanmemberQuestsService clanmemberQuestsService,
                                final ClanmemberActivitiesService clanmemberActivitiesService) {
        this.clanmembersService = clanmembersService;
        this.clanmemberLevelsService = clanmemberLevelsService;
        this.clanmemberMinigamesService = clanmemberMinigamesService;
        this.clanmemberQuestsService = clanmemberQuestsService;
        this.clanmemberActivitiesService = clanmemberActivitiesService;
    }

    @GetMapping("/clanmember")
    public String getPersonalClanmemberLevels(HttpServletRequest request, HttpServletResponse response, Model model) {
        String name = clanmembersService.getCookieValue(request.getCookies());

        if(!StringUtils.hasLength(name)) {
            return "setthecookie";
        }

        fillModel(model, replacePlusToSpace(name), request.getCookies());

        return "clanmember";
    }

    @GetMapping("/clanmember/{name}")
    public String getClanMemberLevels(HttpServletRequest request, Model model, @PathVariable String name) {
        fillModel(model, replacePlusToSpace(name), request.getCookies());

        return "clanmember";
    }

    @GetMapping("/setCookie/{name}")
    public RedirectView setCookie(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, @PathVariable String name) {
        Cookie cookie = new Cookie(COOKIE_NAME, name);
        cookie.setMaxAge(60*60*24*365);
        cookie.setPath("/");

        response.addCookie(cookie);

        log.info("IP {} added cookie for {}", request.getRemoteAddr(), name);

        return new RedirectView("/clanmember/" + replaceEmptySpace(name));
    }

    @GetMapping("/deleteCookie/{name}")
    public RedirectView deleteCookie(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, @PathVariable String name) {
        if(clanmembersService.cookieExists(request.getCookies())) {
            Cookie cookie = new Cookie(COOKIE_NAME, name);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
        }

        return new RedirectView("/clanmember/" + replaceEmptySpace(name));
    }

    private void fillModel(Model model, final String name, final Cookie[] cookies) {
        ClanmemberLevels clanmemberLevels = clanmemberLevelsService.getOneClanmemberLevelsForController(name);
        ClanmemberMinigames clanmemberMinigames = clanmemberMinigamesService.getOneClanmemberMinigames(name);
        ClanmemberQuests clanmemberQuests = clanmemberQuestsService.getOneClanmemberQuests(name);
        ClanmemberActivities clanmemberActivities = clanmemberActivitiesService.getOneClanmemberActivities(name);

        Clanmember clanmember = clanmembersService.getOneNewestClanmember(name);

        boolean ironmanIndicator = clanmember.isIronman();
        boolean hardcoreIronmanIndicator = clanmember.isHardcoreIronman();

        model.addAttribute("versionNumber", version);
        model.addAttribute("clanmemberLevels", clanmemberLevels);
        model.addAttribute("clanmemberMinigames", clanmemberMinigames);
        model.addAttribute("clanmemberQuests", clanmemberQuests);
        model.addAttribute("clanmemberActivities", clanmemberActivities);
        model.addAttribute("clanmemberName", name);

        model.addAttribute("ironmanIndicator", ironmanIndicator);
        model.addAttribute("hardcoreIronmanIndicator", hardcoreIronmanIndicator);

        model.addAttribute("usDateFormatLevels", getDateAsUSString(clanmemberLevels.getDate()));
        model.addAttribute("usDateFormatMinigames", getDateAsUSString(clanmemberMinigames.getDate()));
        model.addAttribute("usDateFormatQuests", getDateAsUSString(clanmemberQuests.getDate()));
        model.addAttribute("usDateFormatActivities", getDateAsUSString(clanmemberActivities.getDate()));

        model.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
        model.addAttribute("cookieExists", clanmembersService.cookieExists(cookies));
        model.addAttribute("cookieUsername", clanmembersService.getCookieValue(cookies));

        model.addAttribute("totalLevel", clanmemberLevelsService.getOverallSkill(clanmemberLevels).getFormattedLevel());
        model.addAttribute("totalExperience", clanmemberLevelsService.getOverallSkill(clanmemberLevels).getFormattedExperience());
        model.addAttribute("combatLevel", clanmemberLevelsService.getCombatLevel(clanmemberLevels));
        model.addAttribute("questPoints", clanmemberQuests.getTotalQuestPointsAsString());
        model.addAttribute("runescore", clanmemberMinigamesService.getRunescoreMinigame(clanmemberMinigames).getFormattedScore());
    }
}
