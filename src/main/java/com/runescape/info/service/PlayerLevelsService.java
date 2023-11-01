package com.runescape.info.service;

import com.runescape.info.model.Level;
import com.runescape.info.entity.PlayerLevels;
import com.runescape.info.model.Skill;
import com.runescape.info.repository.PlayerLevelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PlayerLevelsService {
    private final HiScoresService hiScoresService;
    private final PlayerLevelsRepository playerLevelsRepository;

    private static final Integer START_LEVEL_100_EXPERIENCE = 14391160;
    private static final Integer START_LEVEL_121_EXPERIENCE_ELITE = 83370445;

    @Autowired
    public PlayerLevelsService(final HiScoresService hiScoresService, final PlayerLevelsRepository playerLevelsRepository) {
        this.hiScoresService = hiScoresService;
        this.playerLevelsRepository = playerLevelsRepository;
    }

    public void savePlayerLevelsToDatabase(final String player) {
        playerLevelsRepository.save(getPlayerLevels(player));
    }

    public List<PlayerLevels> getPlayerLevelsFromDatabase(final String player) {
        return playerLevelsRepository.findAllByPlayer(player);
    }

    public PlayerLevels getNewestPlayerLevelsFromDatabase(final String player) {
        return playerLevelsRepository.findFirstByPlayerOrderByDateDesc(player);
    }

    private PlayerLevels getPlayerLevels(final String player) {
        PlayerLevels playerLevels = new PlayerLevels();

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

        playerLevels.setPlayer(player);
        playerLevels.setDate(dateTime.format(dateTimeFormatter));
        playerLevels.setLevels(mapHiscoresToLevelsList(hiScoresService.getLevels(player)));

        return playerLevels;
    }

    private List<Level> mapHiscoresToLevelsList(final List<Integer[]> hiscores) {
        List<Level> levels = hiscores.stream()
                .map(value -> mapIntegerArrayToLevel(hiscores.indexOf(value), value))
                .toList();

        levels.get(0).setLevel(setTotalLevel(levels));

        return levels;
    }

    private Integer setTotalLevel(final List<Level> levels) {
        AtomicReference<Integer> totalLevel = new AtomicReference<>(0);

        levels.forEach(value -> {
            if(value.getSkill() != Skill.OVERALL) {
                totalLevel.set(totalLevel.get() + value.getLevel());
            }
        });

        return totalLevel.get();
    }

    private Level mapIntegerArrayToLevel(final int number, final Integer[] value) {
        Level level = new Level();
        Skill skill = Skill.getSkillByNumber(number);

        level.setSkill(skill);
        level.setRank(value[0]);
        level.setLevel(getCorrectLevel(value[1], skill, value[2]));
        level.setExperience(value[2]);

        return level;
    }

    private Integer getCorrectLevel(final Integer level, final Skill skill, final Integer experience) {
        if(Skill.INVENTION.equals(skill)) {
            return getCorrectLevelForEliteSkill(level, experience);
        }

        return getCorrectLevelForStandardSkill(level, experience);
    }

    private Integer getCorrectLevelForEliteSkill(final Integer level, final Integer experience) {
        if(experience < START_LEVEL_121_EXPERIENCE_ELITE) {
            return level;
        }

        if(experience > 194927409) return 150;
        else if(experience > 189921255) return 149;
        else if(experience > 185007406) return 148;
        else if(experience > 180184770) return 147;
        else if(experience > 175452262) return 146;
        else if(experience > 170808801) return 145;
        else if(experience > 166253312) return 144;
        else if(experience > 161784728) return 143;
        else if(experience > 157401983) return 142;
        else if(experience > 153104021) return 141;
        else if(experience > 148889790) return 140;
        else if(experience > 144758242) return 139;
        else if(experience > 140708338) return 138;
        else if(experience > 136739041) return 137;
        else if(experience > 132849323) return 136;
        else if(experience > 129038159) return 135;
        else if(experience > 125304532) return 134;
        else if(experience > 121647430) return 133;
        else if(experience > 118065845) return 132;
        else if(experience > 114558777) return 131;
        else if(experience > 111125230) return 130;
        else if(experience > 107764216) return 129;
        else if(experience > 104474750) return 128;
        else if(experience > 101255855) return 127;
        else if(experience > 98106559) return 126;
        else if(experience > 95025896) return 125;
        else if(experience > 92012904) return 124;
        else if(experience > 89066630) return 123;
        else if(experience > 86186124) return 122;
        else if(experience > 83370445) return 121;

        throw new RuntimeException("Something weird going on. Level is " + level + " and experience is " + experience + ".");
    }

    private Integer getCorrectLevelForStandardSkill(final Integer level, final Integer experience) {
        if(experience < START_LEVEL_100_EXPERIENCE) {
            return level;
        }

        if(experience > 104273167) return 120;
        else if(experience > 94442737) return 119;
        else if(experience > 85539082) return 118;
        else if(experience > 77474828) return 117;
        else if(experience > 70170840) return 116;
        else if(experience > 63555443) return 115;
        else if(experience > 57563718) return 114;
        else if(experience > 52136869) return 113;
        else if(experience > 47221641) return 112;
        else if(experience > 42769801) return 111;
        else if(experience > 38737661) return 110;
        else if(experience > 35085654) return 109;
        else if(experience > 31777943) return 108;
        else if(experience > 28782069) return 107;
        else if(experience > 26068632) return 106;
        else if(experience > 23611006) return 105;
        else if(experience > 21385073) return 104;
        else if(experience > 19368992) return 103;
        else if(experience > 17542976) return 102;
        else if(experience > 15889109) return 101;
        else if(experience > 14391160) return 100;

        throw new RuntimeException("Something weird going on. Level is " + level + " and experience is " + experience + ".");
    }
}
