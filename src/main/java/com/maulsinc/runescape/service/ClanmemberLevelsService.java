package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.math.LongMath;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.Level;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.entity.ClanmemberLevelsEntity;
import com.maulsinc.runescape.model.Skill;
import com.maulsinc.runescape.model.exception.CorrectLevelException;
import com.maulsinc.runescape.repository.ClanmemberLevelsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.maulsinc.runescape.CommonsService.getDateAsString;

@Service
@Slf4j
public class ClanmemberLevelsService {
    private final ClanmemberLevelsRepository clanmemberLevelsRepository;

    private static final Integer START_LEVEL_100_EXPERIENCE = 14391160;
    private static final Integer START_LEVEL_121_EXPERIENCE_ELITE = 83370445;

    @Autowired
    public ClanmemberLevelsService(final ClanmemberLevelsRepository clanmemberLevelsRepository) {
        this.clanmemberLevelsRepository = clanmemberLevelsRepository;
    }

    @ExecutionTimeLogger
    public ClanmemberLevels getOneClanmemberLevelsForController(final String clanmemberName) {
        return getOneClanmemberLevels(clanmemberName);
    }

    public ClanmemberLevels getOneClanmemberLevels(final String clanmemberName) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = clanmemberLevelsRepository.findFirstByClanmemberOrderByIdDesc(clanmemberName);

        if(clanmemberLevelsEntity == null) {
            return new ClanmemberLevels();
        }

        ClanmemberLevels clanmemberLevels =
                ClanmemberLevels.mapEntityToModel(clanmemberLevelsEntity, getDateAsString(clanmemberLevelsEntity.getId().getDate()));

        setClanmemberLevelsFromToday(clanmemberName, clanmemberLevels.getLevels());
        setClanmemberLevelsFromYesterday(clanmemberName, clanmemberLevels.getLevels());

        return clanmemberLevels;
    }

    public void saveClanmemberLevelsToDatabase(final String clanmember, final List<CSVRecord> levels, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        if(!CollectionUtils.isEmpty(levels)) {
            clanmemberLevelsRepository.save(getClanmemberLevelsEntity(clanmember, levels, levelsIronman, levelsHardcoreIronman));
        }
    }

    public void saveClanmemberLevelsToDatabaseFromProfile(final String clanmember, final JsonNode jsonNode, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        if(!StringUtils.hasLength(clanmember)) {
            return;
        }

        String skillvaluesString = "skillvalues";
        String totalxpString = "totalxp";
        String rankString = "rank";

        if(jsonNode == null || jsonNode.isNull()) {
            log.error("The JsonNode was {}, clanmember {}", jsonNode, clanmember);
            return;
        }

        if(jsonNode.has(skillvaluesString) && jsonNode.has(totalxpString) && jsonNode.has(rankString)) {
            JsonNode skillvalues = jsonNode.get(skillvaluesString);
            Long totalXp = jsonNode.get(totalxpString).asLong();
            Long rank = Long.valueOf(jsonNode.get(rankString).asText().replaceAll(",", ""));

            clanmemberLevelsRepository.save(getClanmemberLevelsEntityFromProfile(clanmember, skillvalues, totalXp, rank, levelsIronman, levelsHardcoreIronman));
        }
    }

    public Level getOverallSkill(final ClanmemberLevels clanmemberLevels) {
        if(clanmemberLevels != null && !CollectionUtils.isEmpty(clanmemberLevels.getLevels())) {
            for (Level level : clanmemberLevels.getLevels()) {
                if(Skill.OVERALL.equals(level.getSkill())) {
                    return level;
                }
            }
        }

        return new Level();
    }

    public String getCombatLevel(final ClanmemberLevels clanmemberLevels) {
        try {
            int attack = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.ATTACK.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 99);
            int strength = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.STRENGTH.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 99);
            int magic = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.MAGIC.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 99) * 2;
            int ranged = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.RANGED.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 99) * 2;
            int necromancy = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.NECROMANCY.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 120) * 2;
            int defence = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.DEFENCE.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 99);
            int constitution = Math.min(Math.toIntExact(clanmemberLevels.getLevels().stream().filter(level -> Skill.CONSTITUTION.equals(level.getSkill())).findFirst().orElse(null).getLevel()), 99);
            int prayer = Math.min(Math.toIntExact(LongMath.divide(clanmemberLevels.getLevels().stream().filter(level -> Skill.PRAYER.equals(level.getSkill())).findFirst().orElse(null).getLevel(), 2, RoundingMode.DOWN)), 49);
            int summoning = Math.min(Math.toIntExact(LongMath.divide(clanmemberLevels.getLevels().stream().filter(level -> Skill.SUMMONING.equals(level.getSkill())).findFirst().orElse(null).getLevel(), 2, RoundingMode.DOWN)), 49);

            int max = NumberUtils.max(attack + strength, magic, ranged, necromancy);

            return String.valueOf((int) Math.floor((1.3 * max + defence + constitution + prayer + summoning) / 4));

        } catch (NullPointerException e) {
            return "--";
        }
    }

    @ExecutionTimeLogger
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

    @ExecutionTimeLogger
    private void setClanmemberLevelsFromYesterday(final String clanmemberName, final List<Level> currentLevels) {
        ObjectId idMin = new ObjectId(Date.from(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant()));
        ObjectId idMax = new ObjectId(Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).minusSeconds(1).atZone(ZoneId.systemDefault()).toInstant()));

        List<ClanmemberLevelsEntity> clanmemberLevelsEntities = clanmemberLevelsRepository.findByObjectIdsAndAllClanmemberLevelsByClanmember(clanmemberName, idMin, idMax);

        List<ClanmemberLevelsEntity> allClanmemberLevelsEntities = clanmemberLevelsEntities.stream()
                .filter(clanmemberLevelsEntity -> clanmemberName.equalsIgnoreCase(clanmemberLevelsEntity.getClanmember()))
                .toList();

        if(!CollectionUtils.isEmpty(allClanmemberLevelsEntities)) {
            setExperienceYesterday(allClanmemberLevelsEntities.get(0), allClanmemberLevelsEntities.get(allClanmemberLevelsEntities.size() - 1), currentLevels);
        }
    }

    private void setExperienceYesterday(final ClanmemberLevelsEntity clanmemberLevelsEntityFirst, final ClanmemberLevelsEntity clanmemberLevelsEntityLast, final List<Level> currentLevels) {
        int count = 0;

        for (Level level : currentLevels) {
            level.setExperienceYesterday(clanmemberLevelsEntityLast.getLevels().get(count).getExperience() - clanmemberLevelsEntityFirst.getLevels().get(count).getExperience());
            count ++;
        }
    }

    ClanmemberLevelsEntity getClanmemberLevelsEntity(final String clanmember, final List<CSVRecord> levels, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = new ClanmemberLevelsEntity();

        clanmemberLevelsEntity.setClanmember(clanmember);
        clanmemberLevelsEntity.setLevels(mapCsvRecordsToLevels(levels, levelsIronman, levelsHardcoreIronman));

        return clanmemberLevelsEntity;
    }

    private List<Level> mapCsvRecordsToLevels(final List<CSVRecord> records, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        List<Level> levels = records.stream()
                .filter(this::filterInvalidCsvRecords)
                .map(csvRecord -> mapOneCsvRecordToLevel(records.indexOf(csvRecord), levelsIronman, levelsHardcoreIronman, csvRecord))
                .toList();

        if (!levels.isEmpty()) {
            levels.get(0).setLevel(setTotalLevel(levels));
        }

        return levels;
    }

    private boolean filterInvalidCsvRecords(final CSVRecord level) {
        try {
            Long.valueOf(level.get(0));
            Long.valueOf(level.get(1));
            Long.valueOf(level.get(2));
        } catch (NumberFormatException exception) {
            return false;
        }

        return true;
    }

    private Level mapOneCsvRecordToLevel(final int index, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman, final CSVRecord csvRecord) {
        Level level = new Level();

        Skill skill = Skill.getSkillByNumber(index);
        Long experience = Long.valueOf(csvRecord.get(2));

        level.setSkill(skill);
        level.setRank(Long.valueOf(csvRecord.get(0)));
        level.setLevel(getCorrectLevel(Long.valueOf(csvRecord.get(1)), skill, experience));
        level.setExperience(experience);

        try {
            level.setRankIronman(Long.valueOf(levelsIronman.get(index).get(0)));
            level.setRankHardcoreIronman(Long.valueOf(levelsHardcoreIronman.get(index).get(0)));
        } catch (IndexOutOfBoundsException ignored) {
        }

        return level;
    }

    ClanmemberLevelsEntity getClanmemberLevelsEntityFromProfile(final String clanmember, final JsonNode skillvalues, final Long totalXp, final Long rank, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        ClanmemberLevelsEntity clanmemberLevelsEntity = new ClanmemberLevelsEntity();

        clanmemberLevelsEntity.setClanmember(clanmember);
        clanmemberLevelsEntity.setLevels(mapJsonNodeToLevels(skillvalues, totalXp, rank, levelsIronman, levelsHardcoreIronman));

        return clanmemberLevelsEntity;
    }

    private List<Level> mapJsonNodeToLevels(final JsonNode skillvalues, final Long totalXp, final Long rank, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        List<Level> levels = new ArrayList<>();

        if(skillvalues == null) {
            return levels;
        }

        List<Level> levelsFromSkillValues = new ArrayList<>();

        if(skillvalues.isArray()) {
            for(JsonNode jsonNode : skillvalues) {
                levelsFromSkillValues.add(mapJsonNodeToLevel(jsonNode, levelsIronman, levelsHardcoreIronman));
            }
        }

        levelsFromSkillValues.sort(Comparator.comparingInt(Level::getNumberFromSkill));

        setOverallInList(levels, totalXp, rank, levelsIronman, levelsHardcoreIronman);

        levels.addAll(levelsFromSkillValues);

        levels.get(0).setLevel(setTotalLevel(levels));

        return levels;
    }

    private Level mapJsonNodeToLevel(final JsonNode jsonNode, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        int index = jsonNode.get("id").asInt() + 1;

        Level level = new Level();

        String experienceAsString = String.valueOf(jsonNode.get("xp").asLong());

        Skill skill = Skill.getSkillByNumber(index);

        if(experienceAsString.length() > 1) {
            experienceAsString = experienceAsString.substring(0, experienceAsString.length() - 1);
        }

        Long experience = Long.valueOf(experienceAsString);

        level.setSkill(skill);
        level.setRank(jsonNode.get("rank").asLong());
        level.setLevel(getCorrectLevel(jsonNode.get("level").asLong(), skill, experience));
        level.setExperience(experience);

        try {
            level.setRankIronman(Long.valueOf(levelsIronman.get(index).get(0)));
            level.setRankHardcoreIronman(Long.valueOf(levelsHardcoreIronman.get(index).get(0)));
        } catch (IndexOutOfBoundsException ignored) {
        }

        return level;
    }

    private void setOverallInList(final List<Level> levels, final Long totalXp, final Long rank, final List<CSVRecord> levelsIronman, final List<CSVRecord> levelsHardcoreIronman) {
        Level level = new Level();

        level.setSkill(Skill.OVERALL);
        level.setRank(rank);
        level.setExperience(totalXp);

        try {
            level.setRankIronman(Long.valueOf(levelsIronman.get(0).get(0)));
            level.setRankHardcoreIronman(Long.valueOf(levelsHardcoreIronman.get(0).get(0)));
        } catch (IndexOutOfBoundsException ignored) {
        }

        levels.add(level);
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
        if(skill == null) {
            return level;
        }

        if(skill.isEliteSkill()) {
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
        else if(experience >= 83370445) return 121L;

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
        else if(experience >= 14391160) return 100L;

        throw new CorrectLevelException("Something weird going on. Level is " + level + " and experience is " + experience + ".");
    }
}
