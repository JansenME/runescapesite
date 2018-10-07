package com.runescape.info.service;

import com.runescape.info.model.AdventurersLogInList;
import com.runescape.info.model.SkillsInList;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemberInfoService {
    private static final Logger logger = LoggerFactory.getLogger(MemberInfoService.class);

    private static final String UNKNOWN = "unknown";
    private static final String INVENTION = "invention";
    private static final String DUNGEONEERING = "dungeoneering";
    private static final String OVERALL = "overall";

    private String gender;

    public ModelAndView getMemberPageInfo(String name) throws FeedException {
        logger.info("In method getMemberPageInfo...");

        List<SkillsInList> memberLevelsList = getMemberLevelsList(name);
        List<AdventurersLogInList> adventurersLogList = getAdventurersLogList(name);

        ModelAndView model = new ModelAndView("member");
        model.addObject("listLevels", memberLevelsList);
        model.addObject("memberName", name);
        model.addObject("listAdventurersLog", adventurersLogList);

        model.addObject("showTableMember", true);

        return model;
    }

    private List<AdventurersLogInList> getAdventurersLogList(String name) throws FeedException {
        logger.info("In method getAdventurersLogList...");
        List<AdventurersLogInList> list = new ArrayList<>();

        URL rssUrl;
        try {
            rssUrl = new URL("http://services.runescape.com/l=0/m=adventurers-log/rssfeed?searchName=" + name);
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build(new XmlReader(rssUrl));

            List<SyndEntry> listSyndEntries;

            try {
                listSyndEntries = (List<SyndEntry>) feed.getEntries();
            } catch (ClassCastException c) {
                logger.error("Feedentries cannot be cast to a List.");
                throw new ClassCastException();
            }

            for (SyndEntry entry : listSyndEntries) {
                LocalDate dateAsLocalDate = LocalDateTime.ofInstant(entry.getPublishedDate().toInstant(), ZoneOffset.UTC).toLocalDate();

                String formattedDate = dateAsLocalDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                String title = entry.getTitle();
                String description = entry.getDescription().getValue().trim().replaceAll("\\s+", " ").replaceAll("\\s,", ",");
                list.add(new AdventurersLogInList(formattedDate, title, description));
            }
        } catch (IOException i) {
            if ("male".equalsIgnoreCase(gender)) {
                list.add(new AdventurersLogInList(null, name + " has set his adventurers log to private mode", ""));
            } else if ("female".equalsIgnoreCase(gender)) {
                list.add(new AdventurersLogInList(null, name + " has set her adventurers log to private mode", ""));
            } else {
                list.add(new AdventurersLogInList(null, name + " has set his/her adventurers log to private mode", ""));
            }
            return list;
        }
        return list;
    }

    private List<SkillsInList> getMemberLevelsList(String name) {
        logger.info("In method getMemberLevelsList...");
        List<SkillsInList> list = new ArrayList<>();

        try {
            URL link = new URL("http://services.runescape.com/m=hiscore/index_lite.ws?player=" + name);
            BufferedReader br = new BufferedReader(new InputStreamReader(link.openStream()));

            String inputLine;
            String skill;

            int counter = 0;

            while ((inputLine = br.readLine()) != null) {
                String[] array = inputLine.split(",");

                String rank = array[0];
                String level = array[1];
                String experience = array[2];

                skill = getCorrectSkillName(counter);

                Locale locale = new Locale("en", "EN");
                NumberFormat numberFormat = NumberFormat.getInstance(locale);

                Long experienceAsLong = Long.parseLong(experience);
                String experienceFormatted = numberFormat.format(experienceAsLong);

                Long rankAsLong = Long.parseLong(rank);
                String rankFormatted = numberFormat.format(rankAsLong);

                Long levelAsLong = Long.parseLong(level);

                if (levelAsLong == 0) level = "1";
                if (experienceAsLong == -1) experienceFormatted = "0";
                if (rankAsLong == -1) rankFormatted = "None";

                String correctVirtualLevel = setCorrectVirtualLevel(experienceAsLong.toString(), skill, level);
                Long correctVirtualLevelAsLong = Long.parseLong(correctVirtualLevel);

                String color = getTheCorrectColor(skill, experienceAsLong.toString(), correctVirtualLevelAsLong.toString());

                String totalVirtualLevel = null;
                if (OVERALL.equals(skill)) totalVirtualLevel = Integer.toString(getTotalVirtualLevel(name));

                list.add(new SkillsInList(skill, correctVirtualLevel, experienceFormatted, rankFormatted, totalVirtualLevel, color));
                counter++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return list;
        } catch (IOException e) {
            logger.error(e.getMessage());
            list.add(new SkillsInList("", "", "", "", "", ""));
            return list;
        }
        return list;
    }

    private String getTheCorrectColor(String skill, String experience, String correctVirtualLevel) {
        logger.info("In method getTheCorrectColor...");
        final String colorRed = "red";
        final String colorLimegreen = "limegreen";
        final String colorBold = "bold";
        final String colorNormal = "normal";

        switch (skill) {
            case OVERALL:
                return colorNormal;

            case DUNGEONEERING:
                if (Long.parseLong(experience) == 200000000) return colorRed;
                else if (Long.parseLong(correctVirtualLevel) == 120) return colorLimegreen;
                else return colorNormal;

            case INVENTION:
                if (Long.parseLong(experience) == 200000000) return colorRed;
                else if (Long.parseLong(correctVirtualLevel) == 150) return colorLimegreen;
                else if (Long.parseLong(correctVirtualLevel) >= 120) return colorBold;
                else return colorNormal;

            default:
                if (Long.parseLong(experience) == 200000000) return colorRed;
                else if (Long.parseLong(correctVirtualLevel) == 120) return colorLimegreen;
                else if (Long.parseLong(correctVirtualLevel) >= 99) return colorBold;
                else return colorNormal;
        }
    }

    private int getTotalVirtualLevel(String nameMember) {
        logger.info("In method getTotalVirtualLevel...");
        int totalLevel = 0;
        int totalVirtualLevel = 0;
        try {
            URL link = new URL("http://services.runescape.com/m=hiscore/index_lite.ws?player=" + nameMember);
            BufferedReader br = new BufferedReader(new InputStreamReader(link.openStream()));

            String inputLine;
            String skill;
            int counter = 0;
            while ((inputLine = br.readLine()) != null) {
                String[] array = inputLine.split(",");

                skill = getCorrectSkillName(counter);

                Long experienceLevel = Long.parseLong(array[2]);

                int level = Integer.parseInt(array[1]);

                if (level == 0) {
                    array[1] = "1";
                }

                String correctVirtualLevel = setCorrectVirtualLevel(experienceLevel.toString(), skill, array[1]);

                totalLevel = Integer.parseInt(correctVirtualLevel);

                if (!skill.equals(OVERALL)) {
                    totalVirtualLevel += totalLevel;
                }
                counter++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return totalVirtualLevel;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return totalVirtualLevel;
    }

    private static String getCorrectSkillName(int counter) {
        logger.info("In method getCorrectSkillName...");
        switch (counter) {
            case 0:
                return OVERALL;
            case 1:
                return "attack";
            case 2:
                return "defence";
            case 3:
                return "strength";
            case 4:
                return "constitution";
            case 5:
                return "ranged";
            case 6:
                return "prayer";
            case 7:
                return "magic";
            case 8:
                return "cooking";
            case 9:
                return "woodcutting";
            case 10:
                return "fletching";
            case 11:
                return "fishing";
            case 12:
                return "firemaking";
            case 13:
                return "crafting";
            case 14:
                return "smithing";
            case 15:
                return "mining";
            case 16:
                return "herblore";
            case 17:
                return "agility";
            case 18:
                return "thieving";
            case 19:
                return "slayer";
            case 20:
                return "farming";
            case 21:
                return "runecrafting";
            case 22:
                return "hunter";
            case 23:
                return "construction";
            case 24:
                return "summoning";
            case 25:
                return DUNGEONEERING;
            case 26:
                return "divination";
            case 27:
                return INVENTION;
            default:
                return UNKNOWN;
        }
    }

    private static String setCorrectVirtualLevel(final String totalExperience, final String skill, final String level) {
        logger.info("In method setCorrectVirtualLevel...");
        String levelToReturn = level;

        if (INVENTION.equals(skill) && Long.parseLong(totalExperience) >= 83370445)
            levelToReturn = setVirtualLevelForInvention(totalExperience, level);
        if ((!INVENTION.equals(skill) && !OVERALL.equals(skill)) && Long.parseLong(totalExperience) >= 14391160)
            levelToReturn = setVirtualLevelForTheRest(totalExperience, level);

        return levelToReturn;
    }

    private static String setVirtualLevelForInvention(final String totalExperience, final String level) {
        logger.info("In method setVirtualLevelForInvention...");
        if (Long.parseLong(totalExperience) >= 194927409) return "150";
        if (Long.parseLong(totalExperience) >= 189921255) return "149";
        if (Long.parseLong(totalExperience) >= 185007406) return "148";
        if (Long.parseLong(totalExperience) >= 180184770) return "147";
        if (Long.parseLong(totalExperience) >= 175452262) return "146";
        if (Long.parseLong(totalExperience) >= 170808801) return "145";
        if (Long.parseLong(totalExperience) >= 166253312) return "144";
        if (Long.parseLong(totalExperience) >= 161784728) return "143";
        if (Long.parseLong(totalExperience) >= 157401983) return "142";
        if (Long.parseLong(totalExperience) >= 153104021) return "141";
        if (Long.parseLong(totalExperience) >= 148889790) return "140";
        if (Long.parseLong(totalExperience) >= 144758242) return "139";
        if (Long.parseLong(totalExperience) >= 140708338) return "138";
        if (Long.parseLong(totalExperience) >= 136739041) return "137";
        if (Long.parseLong(totalExperience) >= 132849323) return "136";
        if (Long.parseLong(totalExperience) >= 129038159) return "135";
        if (Long.parseLong(totalExperience) >= 125304532) return "134";
        if (Long.parseLong(totalExperience) >= 121647430) return "133";
        if (Long.parseLong(totalExperience) >= 118065845) return "132";
        if (Long.parseLong(totalExperience) >= 114558777) return "131";
        if (Long.parseLong(totalExperience) >= 111125230) return "130";
        if (Long.parseLong(totalExperience) >= 107764216) return "129";
        if (Long.parseLong(totalExperience) >= 104474750) return "128";
        if (Long.parseLong(totalExperience) >= 101255855) return "127";
        if (Long.parseLong(totalExperience) >= 98106559) return "126";
        if (Long.parseLong(totalExperience) >= 95025896) return "125";
        if (Long.parseLong(totalExperience) >= 92012904) return "124";
        if (Long.parseLong(totalExperience) >= 89066630) return "123";
        if (Long.parseLong(totalExperience) >= 86186124) return "122";
        if (Long.parseLong(totalExperience) >= 83370445) return "121";

        return level;
    }

    private static String setVirtualLevelForTheRest(final String totalExperience, final String level) {
        logger.info("In method setVirtualLevelForTheRest...");
        if (Long.parseLong(totalExperience) >= 104273167) return "120";
        if (Long.parseLong(totalExperience) >= 94442737) return "119";
        if (Long.parseLong(totalExperience) >= 85539082) return "118";
        if (Long.parseLong(totalExperience) >= 77474828) return "117";
        if (Long.parseLong(totalExperience) >= 70170840) return "116";
        if (Long.parseLong(totalExperience) >= 63555443) return "115";
        if (Long.parseLong(totalExperience) >= 57563718) return "114";
        if (Long.parseLong(totalExperience) >= 52136869) return "113";
        if (Long.parseLong(totalExperience) >= 47221641) return "112";
        if (Long.parseLong(totalExperience) >= 42769801) return "111";
        if (Long.parseLong(totalExperience) >= 38737661) return "110";
        if (Long.parseLong(totalExperience) >= 35085654) return "109";
        if (Long.parseLong(totalExperience) >= 31777943) return "108";
        if (Long.parseLong(totalExperience) >= 28782069) return "107";
        if (Long.parseLong(totalExperience) >= 26068632) return "106";
        if (Long.parseLong(totalExperience) >= 23611006) return "105";
        if (Long.parseLong(totalExperience) >= 21385073) return "104";
        if (Long.parseLong(totalExperience) >= 19368992) return "103";
        if (Long.parseLong(totalExperience) >= 17542976) return "102";
        if (Long.parseLong(totalExperience) >= 15889109) return "101";
        if (Long.parseLong(totalExperience) >= 14391160) return "100";

        return level;
    }
}
