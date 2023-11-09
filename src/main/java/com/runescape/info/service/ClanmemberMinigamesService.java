package com.runescape.info.service;

import com.runescape.info.CommonsService;
import com.runescape.info.model.ClanmemberMinigames;
import com.runescape.info.model.Minigame;
import com.runescape.info.model.MinigameEnum;
import com.runescape.info.model.entity.ClanmemberMinigamesEntity;
import com.runescape.info.repository.ClanmemberMinigamesRepository;
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
                .map(record -> mapOneCsvRecordToMinigame(records.indexOf(record), record))
                .toList();
    }

    private Minigame mapOneCsvRecordToMinigame(final int index, final CSVRecord record) {
        Minigame minigame = new Minigame();

        minigame.setMinigameEnum(MinigameEnum.getMinigameEnumByNumber(index));
        minigame.setRank(Long.valueOf(record.get(0)));
        minigame.setScore(Long.valueOf(record.get(1)));

        return minigame;
    }
}
