package com.maulsinc.runescape.service;

import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.*;
import com.maulsinc.runescape.repository.ClanmemberMinigamesRepository;
import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.maulsinc.runescape.CommonsService.getDateAsString;

@Service
public class ClanmemberMinigamesService {
    private final ClanmemberMinigamesRepository clanmemberMinigamesRepository;

    @Autowired
    public ClanmemberMinigamesService(final ClanmemberMinigamesRepository clanmemberMinigamesRepository) {
        this.clanmemberMinigamesRepository = clanmemberMinigamesRepository;
    }

    @ExecutionTimeLogger
    public ClanmemberMinigames getOneClanmemberMinigames(final String clanmemberName) {
        ClanmemberMinigamesEntity clanmemberMinigamesEntity = clanmemberMinigamesRepository.findFirstByClanmemberOrderByIdDesc(clanmemberName);

        if(clanmemberMinigamesEntity == null) {
            return new ClanmemberMinigames();
        }

        return ClanmemberMinigames.mapEntityToModel(clanmemberMinigamesEntity, getDateAsString(clanmemberMinigamesEntity.getId().getDate()));
    }

    public void saveClanmemberMinigamesToDatabase(final String clanmember, final List<CSVRecord> minigames, final List<CSVRecord> minigamesIronman, final List<CSVRecord> minigamesHardcoreIronman) {
        if(StringUtils.hasLength(clanmember) && !CollectionUtils.isEmpty(minigames)) {
            clanmemberMinigamesRepository.save(getClanmemberMinigamesEntity(clanmember, minigames, minigamesIronman, minigamesHardcoreIronman));
        }
    }

    public List<Pair<String, String>> getTop10Runescore(final List<Clanmember> clanmembers) {
        if(CollectionUtils.isEmpty(clanmembers)) {
            return new ArrayList<>();
        }

        List<Pair<String, Long>> finalList = sortAndLimitList(createPairList(getAllClanmemberMinigames(clanmembers)));

        return formatNumbersInList(finalList);
    }

    public Minigame getRunescoreMinigame(final ClanmemberMinigames clanmemberMinigames) {
        if(clanmemberMinigames != null && !CollectionUtils.isEmpty(clanmemberMinigames.getMinigames())) {
            for (Minigame minigame : clanmemberMinigames.getMinigames()) {
                if(MinigameName.RUNESCORE.equals(minigame.getMinigameName())) {
                    return minigame;
                }
            }
        }

        return new Minigame();
    }

    ClanmemberMinigamesEntity getClanmemberMinigamesEntity(final String clanmember, final List<CSVRecord> minigames, final List<CSVRecord> minigamesIronman, final List<CSVRecord> minigamesHardcoreIronman) {
        ClanmemberMinigamesEntity clanmemberMinigamesEntity = new ClanmemberMinigamesEntity();

        clanmemberMinigamesEntity.setClanmember(clanmember);
        clanmemberMinigamesEntity.setMinigames(mapCsvRecordsToMinigames(minigames, minigamesIronman, minigamesHardcoreIronman));

        return clanmemberMinigamesEntity;
    }

    private List<Minigame> mapCsvRecordsToMinigames(final List<CSVRecord> records, final List<CSVRecord> minigamesIronman, final List<CSVRecord> minigamesHardcoreIronman) {
        return records.stream()
                .map(csvRecord -> mapOneCsvRecordToMinigame(records.indexOf(csvRecord), csvRecord, minigamesIronman, minigamesHardcoreIronman))
                .toList();
    }

    private Minigame mapOneCsvRecordToMinigame(final int index, final CSVRecord csvRecord, final List<CSVRecord> minigamesIronman, final List<CSVRecord> minigamesHardcoreIronman) {
        Minigame minigame = new Minigame();

        minigame.setMinigameName(MinigameName.getMinigameNameByNumber(index));
        minigame.setRank(Long.valueOf(csvRecord.get(0)));
        minigame.setScore(Long.valueOf(csvRecord.get(1)));

        try {
            minigame.setRankIronman(Long.valueOf(minigamesIronman.get(index).get(0)));
            minigame.setRankHardcoreIronman(Long.valueOf(minigamesHardcoreIronman.get(index).get(0)));
        } catch (IndexOutOfBoundsException ignored) {
        }

        return minigame;
    }

    private List<ClanmemberMinigames> getAllClanmemberMinigames(final List<Clanmember> clanmembers) {
        List<ClanmemberMinigames> minigames = new ArrayList<>();

        clanmembers.forEach(clanmember -> minigames.add(getOneClanmemberMinigames(clanmember.getName())));

        return minigames;
    }

    private List<Pair<String, Long>> createPairList(final List<ClanmemberMinigames> minigames) {
        List<Pair<String, Long>> minigamesPairList = new ArrayList<>();

        minigames.forEach(clanmemberMinigames -> minigamesPairList.add(createNewMinigamePair(clanmemberMinigames)));

        return minigamesPairList;
    }

    private Pair<String, Long> createNewMinigamePair(final ClanmemberMinigames clanmemberMinigames) {
        Minigame runescore = getRunescoreMinigame(clanmemberMinigames);

        if(!StringUtils.hasLength(clanmemberMinigames.getClanmember()) || runescore.getScore() == null) {
            return Pair.of("", 0L);
        }

        return Pair.of(clanmemberMinigames.getClanmember(), runescore.getScore());
    }

    private List<Pair<String, Long>> sortAndLimitList(final List<Pair<String, Long>> runescores) {
        runescores.sort((runescore1, runescore2) -> Math.toIntExact(runescore2.getSecond() - runescore1.getSecond()));

        return runescores.stream()
                .limit(10)
                .filter(runescore -> runescore.getSecond() > 0)
                .toList();
    }

    private List<Pair<String, String>> formatNumbersInList(final List<Pair<String, Long>> runescores) {
        List<Pair<String, String>> formattedList = new ArrayList<>();

        runescores.forEach(runescore -> formattedList.add(formatNumber(runescore)));

        return formattedList;
    }

    private Pair<String, String> formatNumber(Pair<String, Long> runescore) {
        return Pair.of(runescore.getFirst(), CommonsService.getFormattedNumber(runescore.getSecond()));
    }
}
