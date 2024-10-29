package com.maulsinc.runescape.service;

import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.*;
import com.maulsinc.runescape.repository.ClanmemberMinigamesRepository;
import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

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

        return ClanmemberMinigames.mapEntityToModel(clanmemberMinigamesEntity, CommonsService.getDateAsString(clanmemberMinigamesEntity.getId().getDate()));
    }

    public void saveClanmemberMinigamesToDatabase(final String clanmember, final List<CSVRecord> minigames, final List<CSVRecord> minigamesIronman, final List<CSVRecord> minigamesHardcoreIronman) {
        if(StringUtils.hasLength(clanmember) && !CollectionUtils.isEmpty(minigames)) {
            clanmemberMinigamesRepository.save(getClanmemberMinigamesEntity(clanmember, minigames, minigamesIronman, minigamesHardcoreIronman));
        }
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
        minigame.setRankIronman(Long.valueOf(minigamesIronman.get(index).get(0)));
        minigame.setRankHardcoreIronman(Long.valueOf(minigamesHardcoreIronman.get(index).get(0)));
        minigame.setScore(Long.valueOf(csvRecord.get(1)));

        return minigame;
    }
}
