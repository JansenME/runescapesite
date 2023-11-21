package com.maulsinc.runescape.service;

import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.ClanmemberMinigames;
import com.maulsinc.runescape.model.Minigame;
import com.maulsinc.runescape.model.MinigameName;
import com.maulsinc.runescape.repository.ClanmemberMinigamesRepository;
import com.maulsinc.runescape.model.entity.ClanmemberMinigamesEntity;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    public void saveClanmemberMinigamesToDatabase(final String clanmember, final List<CSVRecord> minigames) {
        if(!CollectionUtils.isEmpty(minigames)) {
            clanmemberMinigamesRepository.save(getClanmemberMinigamesEntity(clanmember, minigames));
        }
    }

    private ClanmemberMinigamesEntity getClanmemberMinigamesEntity(final String clanmember, final List<CSVRecord> minigames) {
        ClanmemberMinigamesEntity clanmemberMinigamesEntity = new ClanmemberMinigamesEntity();

        clanmemberMinigamesEntity.setClanmember(clanmember);
        clanmemberMinigamesEntity.setMinigames(mapCsvRecordsToMinigames(minigames));

        return clanmemberMinigamesEntity;
    }

    private List<Minigame> mapCsvRecordsToMinigames(final List<CSVRecord> records) {
        return records.stream()
                .map(csvRecord -> mapOneCsvRecordToMinigame(records.indexOf(csvRecord), csvRecord))
                .toList();
    }

    private Minigame mapOneCsvRecordToMinigame(final int index, final CSVRecord csvRecord) {
        Minigame minigame = new Minigame();

        minigame.setMinigameName(MinigameName.getMinigameNameByNumber(index));
        minigame.setRank(Long.valueOf(csvRecord.get(0)));
        minigame.setScore(Long.valueOf(csvRecord.get(1)));

        return minigame;
    }
}
