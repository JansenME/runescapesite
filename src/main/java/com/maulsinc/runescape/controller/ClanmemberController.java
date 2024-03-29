package com.maulsinc.runescape.controller;

import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.ClanmemberMinigames;
import com.maulsinc.runescape.model.ClanmemberQuests;
import com.maulsinc.runescape.service.ClanmemberLevelsService;
import com.maulsinc.runescape.service.ClanmemberMinigamesService;
import com.maulsinc.runescape.service.ClanmemberQuestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Calendar;

@Controller
public class ClanmemberController {
    @Value("${app.version:unknown}")
    String version;

    private final ClanmemberLevelsService clanmemberLevelsService;
    private final ClanmemberMinigamesService clanmemberMinigamesService;
    private final ClanmemberQuestsService clanmemberQuestsService;

    @Autowired
    public ClanmemberController(final ClanmemberLevelsService clanmemberLevelsService,
                                final ClanmemberMinigamesService clanmemberMinigamesService,
                                final ClanmemberQuestsService clanmemberQuestsService) {
        this.clanmemberLevelsService = clanmemberLevelsService;
        this.clanmemberMinigamesService = clanmemberMinigamesService;
        this.clanmemberQuestsService = clanmemberQuestsService;
    }

    @GetMapping("/clanmember/{name}")
    public String getClanMemberLevels(Model model, @PathVariable String name) {
        ClanmemberLevels clanmemberLevels = clanmemberLevelsService.getOneClanmemberLevels(name);
        ClanmemberMinigames clanmemberMinigames = clanmemberMinigamesService.getOneClanmemberMinigames(name);
        ClanmemberQuests clanmemberQuests = clanmemberQuestsService.getOneClanmemberQuests(name);

        model.addAttribute("versionNumber", version);
        model.addAttribute("clanmemberLevels", clanmemberLevels);
        model.addAttribute("clanmemberMinigames", clanmemberMinigames);
        model.addAttribute("clanmemberQuests", clanmemberQuests);
        model.addAttribute("clanmemberName", name);

        model.addAttribute("usDateFormatLevels", CommonsService.getDateAsUSString(clanmemberLevels.getDate()));
        model.addAttribute("usDateFormatMinigames", CommonsService.getDateAsUSString(clanmemberMinigames.getDate()));
        model.addAttribute("usDateFormatQuests", CommonsService.getDateAsUSString(clanmemberQuests.getDate()));

        model.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));

        model.addAttribute("totalLevel", clanmemberLevelsService.getOverallSkill(clanmemberLevels).getFormattedLevel());
        model.addAttribute("totalExperience", clanmemberLevelsService.getOverallSkill(clanmemberLevels).getFormattedExperience());
        model.addAttribute("combatLevel", clanmemberLevelsService.getCombatLevel(clanmemberLevels));
        model.addAttribute("questPoints", clanmemberQuests.getTotalQuestPointsAsString());
        model.addAttribute("runescore", clanmemberMinigamesService.getRunescoreMinigame(clanmemberMinigames).getFormattedScore());

        return "clanmember";
    }
}
