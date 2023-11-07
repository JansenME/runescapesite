package com.runescape.info.service;

import com.runescape.info.model.Clanmember;
import com.runescape.info.model.ClanmemberLevels;
import com.runescape.info.model.Level;
import com.runescape.info.model.entity.ClanmemberLevelsEntity;
import com.runescape.info.model.Skill;
import com.runescape.info.model.exception.CorrectLevelException;
import com.runescape.info.repository.ClanmemberLevelsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class ClanmemberLevelsService {
    private final HiScoresService hiScoresService;
    private final ClanmembersService clanmembersService;
    private final ClanmemberLevelsRepository clanmemberLevelsRepository;

    private static final Integer START_LEVEL_100_EXPERIENCE = 14391160;
    private static final Integer START_LEVEL_121_EXPERIENCE_ELITE = 83370445;

    @Autowired
    public ClanmemberLevelsService(final HiScoresService hiScoresService,
                                   final ClanmembersService clanmembersService,
                                   final ClanmemberLevelsRepository clanmemberLevelsRepository) {
        this.hiScoresService = hiScoresService;
        this.clanmembersService = clanmembersService;
        this.clanmemberLevelsRepository = clanmemberLevelsRepository;
    }

    public ClanmemberLevels getOneClanmemberLevels(final String clanmemberName) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsRepository.findFirstByClanmemberOrderByIdDesc(clanmemberName);

        if(clanmemberLevelsEntity == null) {
            return new ClanmemberLevels();
        }

        ClanmemberLevels clanmemberLevels = ClanmemberLevels.mapEntityToModel(clanmemberLevelsEntity, getDateAsString(clanmemberLevelsEntity));

        setClanmemberLevelsFromToday(clanmemberName, clanmemberLevels.getLevels());

        return clanmemberLevels;
    }

    @Scheduled(cron = "0 */20 * * * *")
    public void getAllClanmembersAndSavePlayerLevels() {
        List<Clanmember> clanmembers = clanmembersService.getAllClanmembers().getSecond();

        if(!CollectionUtils.isEmpty(clanmembers)) {
            clanmembers.forEach(this::savePlayerLevelsToDatabase);
        }
    }

    private String getDateAsString(ClanmemberLevelsEntity clanmemberLevelsEntity) {
        Date date = clanmemberLevelsEntity.getId().getDate();

        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy h:mm a z");
        return format.format(date);
    }

    private void setClanmemberLevelsFromToday(final String clanmemberName, final List<Level> currentLevels) {
        Date min = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());

        ObjectId idMin = new ObjectId(min);
        ObjectId idMax = new ObjectId(new Date());
        List<ClanmemberLevelsEntity> clanmemberLevelsEntities = clanmemberLevelsRepository.findByObjectIdsAndAllClanmemberLevelsByClanmember(clanmemberName, idMin, idMax);

        ClanmemberLevelsEntity firstClanmemberLevelsEntity = clanmemberLevelsEntities.stream()
                .filter(clanmemberLevelsEntity -> clanmemberName.equalsIgnoreCase(clanmemberLevelsEntity.getClanmember()))
                .toList()
                .get(0);

        int count = 0;
        for (Level currentLevel : currentLevels) {
            Level oldLevel = firstClanmemberLevelsEntity.getLevels().get(count);
            currentLevel.setExperienceToday(currentLevel.getExperience() - oldLevel.getExperience());
            count++;
        }
    }

    private void savePlayerLevelsToDatabase(final Clanmember clanmember) {
        ClanmemberLevelsEntity entity = getClanmemberLevels(clanmember.getName());
        if(!entity.getLevels().isEmpty()) {
            clanmemberLevelsRepository.save(entity);
            log.info("Saved info for " + clanmember + " at " + LocalDateTime.now());
        } else {
            log.info(clanmember + " was not found in Runescape API");
        }
    }

    private ClanmemberLevelsEntity getClanmemberLevels(final String clanmember) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = new ClanmemberLevelsEntity();

        clanmemberLevelsEntity.setClanmember(clanmember);
        clanmemberLevelsEntity.setLevels(mapCsvRecordsToLevels(hiScoresService.getLevels(clanmember)));

        return clanmemberLevelsEntity;
    }

    private List<Level> mapCsvRecordsToLevels(final List<CSVRecord> records) {
        List<Level> levels = records.stream()
                .map(record -> mapOneCsvRecordToLevel(records.indexOf(record), record))
                .toList();

        if (!levels.isEmpty()) {
            levels.get(0).setLevel(setTotalLevel(levels));
        }

        return levels;
    }

    private Level mapOneCsvRecordToLevel(final int index, final CSVRecord record) {
        Level level = new Level();

        Skill skill = Skill.getSkillByNumber(index);
        Long experience = Long.valueOf(record.get(2));

        level.setSkill(skill);
        level.setRank(Long.valueOf(record.get(0)));
        level.setLevel(getCorrectLevel(Long.valueOf(record.get(1)), skill, experience));
        level.setExperience(experience);

        return level;
    }

    private Long setTotalLevel(final List<Level> levels) {
        AtomicLong totalLevel = new AtomicLong(0);

        levels.forEach(value -> {
            if(value.getSkill() != Skill.OVERALL) {
                totalLevel.set(totalLevel.get() + value.getLevel());
            }
        });

        return totalLevel.get();
    }

    private Long getCorrectLevel(final Long level, final Skill skill, final Long experience) {
        if(Skill.INVENTION.equals(skill)) {
            return getCorrectLevelForEliteSkill(level, experience);
        }

        return getCorrectLevelForStandardSkill(level, experience);
    }

    private Long getCorrectLevelForEliteSkill(final Long level, final Long experience) {
        if(experience < START_LEVEL_121_EXPERIENCE_ELITE) {
            return level;
        }

        if(experience > 194927409) return 150L;
        else if(experience > 189921255) return 149L;
        else if(experience > 185007406) return 148L;
        else if(experience > 180184770) return 147L;
        else if(experience > 175452262) return 146L;
        else if(experience > 170808801) return 145L;
        else if(experience > 166253312) return 144L;
        else if(experience > 161784728) return 143L;
        else if(experience > 157401983) return 142L;
        else if(experience > 153104021) return 141L;
        else if(experience > 148889790) return 140L;
        else if(experience > 144758242) return 139L;
        else if(experience > 140708338) return 138L;
        else if(experience > 136739041) return 137L;
        else if(experience > 132849323) return 136L;
        else if(experience > 129038159) return 135L;
        else if(experience > 125304532) return 134L;
        else if(experience > 121647430) return 133L;
        else if(experience > 118065845) return 132L;
        else if(experience > 114558777) return 131L;
        else if(experience > 111125230) return 130L;
        else if(experience > 107764216) return 129L;
        else if(experience > 104474750) return 128L;
        else if(experience > 101255855) return 127L;
        else if(experience > 98106559) return 126L;
        else if(experience > 95025896) return 125L;
        else if(experience > 92012904) return 124L;
        else if(experience > 89066630) return 123L;
        else if(experience > 86186124) return 122L;
        else if(experience > 83370445) return 121L;

        throw new CorrectLevelException("Something weird going on. Level is " + level + " and experience is " + experience + ".");
    }

    private Long getCorrectLevelForStandardSkill(final Long level, final Long experience) {
        if(experience < START_LEVEL_100_EXPERIENCE) {
            return level;
        }

        if(experience > 104273167) return 120L;
        else if(experience > 94442737) return 119L;
        else if(experience > 85539082) return 118L;
        else if(experience > 77474828) return 117L;
        else if(experience > 70170840) return 116L;
        else if(experience > 63555443) return 115L;
        else if(experience > 57563718) return 114L;
        else if(experience > 52136869) return 113L;
        else if(experience > 47221641) return 112L;
        else if(experience > 42769801) return 111L;
        else if(experience > 38737661) return 110L;
        else if(experience > 35085654) return 109L;
        else if(experience > 31777943) return 108L;
        else if(experience > 28782069) return 107L;
        else if(experience > 26068632) return 106L;
        else if(experience > 23611006) return 105L;
        else if(experience > 21385073) return 104L;
        else if(experience > 19368992) return 103L;
        else if(experience > 17542976) return 102L;
        else if(experience > 15889109) return 101L;
        else if(experience > 14391160) return 100L;

        throw new CorrectLevelException("Something weird going on. Level is " + level + " and experience is " + experience + ".");
    }
}
