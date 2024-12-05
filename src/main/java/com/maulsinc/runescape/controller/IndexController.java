package com.maulsinc.runescape.controller;

import com.maulsinc.runescape.model.Clanmember;
import com.maulsinc.runescape.service.ClanmembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.maulsinc.runescape.CommonsService.getDateAsUSString;

@Controller
public class IndexController {
    @Value("${app.version:unknown}")
    String version;

    private final ClanmembersService clanmembersService;

    @Autowired
    public IndexController(final ClanmembersService clanmembersService) {
        this.clanmembersService = clanmembersService;
    }

    @GetMapping(value={"", "/", "/index"})
    public String index(Model model) {
        Pair<String, List<Clanmember>> clanmembers = clanmembersService.getAllClanmembers();

        model.addAttribute("versionNumber", version);
        model.addAttribute("clanmembers", clanmembers);
        model.addAttribute("usDateFormat", getDateAsUSString(clanmembers.getFirst()));
        model.addAttribute("top5ExperienceToday", clanmembersService.getClanmembersTop5Experience());

        model.addAttribute("currentYear", new SimpleDateFormat("yyyy").format(new Date()));

        return "index";
    }

    @GetMapping("/alpaca")
    public String alpaca(Model model) {
        return "alpaca";
    }
}
