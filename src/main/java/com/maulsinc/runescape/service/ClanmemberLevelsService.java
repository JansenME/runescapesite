package com.maulsinc.runescape.service;

import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.model.Level;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.entity.ClanmemberLevelsEntity;
import com.maulsinc.runescape.model.Skill;
import com.maulsinc.runescape.model.exception.CorrectLevelException;
import com.maulsinc.runescape.repository.ClanmemberLevelsRepository;
import org.apache.commons.csv.CSVRecord;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ClanmemberLevelsService {
    private final ClanmemberLevelsRepository clanmemberLevelsRepository;

    private static final Integer START_LEVEL_100_EXPERIENCE = 14391160;
    private static final Integer START_LEVEL_121_EXPERIENCE_ELITE = 83370445;

    @Autowired
    public ClanmemberLevelsService(final ClanmemberLevelsRepository clanmemberLevelsRepository) {
        this.clanmemberLevelsRepository = clanmemberLevelsRepository;
    }

    public ClanmemberLevels getOneClanmemberLevels(final String clanmemberName) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsRepository.findFirstByClanmemberOrderByIdDesc(clanmemberName);

        if(clanmemberLevelsEntity == null) {
            return new ClanmemberLevels();
        }

        ClanmemberLevels clanmemberLevels =
                ClanmemberLevels.mapEntityToModel(clanmemberLevelsEntity, CommonsService.getDateAsString(clanmemberLevelsEntity.getId().getDate()));

        setClanmemberLevelsFromToday(clanmemberName, clanmemberLevels.getLevels());

        return clanmemberLevels;
    }

    public void saveClanmemberLevelsToDatabase(final String clanmember, final List<CSVRecord> levels) {
        if(!CollectionUtils.isEmpty(levels)) {
            clanmemberLevelsRepository.save(getClanmemberLevelsEntity(clanmember, levels));
        }
    }

    private void setClanmemberLevelsFromToday(final String clanmemberName, final List<Level> currentLevels) {
        Date min = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant());

        ObjectId idMin = new ObjectId(min);
        ObjectId idMax = new ObjectId(new Date());
        List<ClanmemberLevelsEntity> clanmemberLevelsEntities = clanmemberLevelsRepository.findByObjectIdsAndAllClanmemberLevelsByClanmember(clanmemberName, idMin, idMax);

        List<ClanmemberLevelsEntity> allClanmemberLevelsEntities = clanmemberLevelsEntities.stream()
                .filter(clanmemberLevelsEntity -> clanmemberName.equalsIgnoreCase(clanmemberLevelsEntity.getClanmember()))
                .toList();

        if(!CollectionUtils.isEmpty(allClanmemberLevelsEntities)) {
            setExperienceToday(allClanmemberLevelsEntities.get(0), currentLevels);
        }
    }

    private void setExperienceToday(final ClanmemberLevelsEntity firstClanmemberLevelsEntity, final List<Level> currentLevels) {
        int count = 0;

        for (Level currentLevel : currentLevels) {
            Level oldLevel = firstClanmemberLevelsEntity.getLevels().get(count);
            currentLevel.setExperienceToday(currentLevel.getExperience() - oldLevel.getExperience());
            count++;
        }
    }

    private ClanmemberLevelsEntity getClanmemberLevelsEntity(final String clanmember, final List<CSVRecord> levels) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = new ClanmemberLevelsEntity();

        clanmemberLevelsEntity.setClanmember(clanmember);
        clanmemberLevelsEntity.setLevels(mapCsvRecordsToLevels(levels));

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
