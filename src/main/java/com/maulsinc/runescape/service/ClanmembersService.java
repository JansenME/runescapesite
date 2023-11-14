package com.maulsinc.runescape.service;

import com.google.common.collect.Lists;
import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.repository.ClanmembersRepository;
import com.maulsinc.runescape.model.entity.ClanmembersEntity;
import com.maulsinc.runescape.model.Clanmember;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClanmembersService {
    private final ConnectionService connectionService;
    private final ClanmemberLevelsService clanmemberLevelsService;
    private final ClanmemberMinigamesService clanmemberMinigamesService;
    private final ClanmembersRepository clanmembersRepository;

    @Autowired
    public ClanmembersService(final ConnectionService connectionService,
                              final ClanmemberLevelsService clanmemberLevelsService,
                              final ClanmemberMinigamesService clanmemberMinigamesService,
                              final ClanmembersRepository clanmembersRepository) {
        this.connectionService = connectionService;
        this.clanmemberLevelsService = clanmemberLevelsService;
        this.clanmemberMinigamesService = clanmemberMinigamesService;
        this.clanmembersRepository = clanmembersRepository;
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void saveAllClanmembersFromRunescape() {
        ClanmembersEntity entity = clanmembersRepository.save(getClanmembersFromRunescape());
        log.info("Saved " + entity.getClanmembers().size() + " clanmembers from Mauls Inc to database.");
    }

    @Scheduled(cron = "0 */20 * * * *")
    public void getAllClanmembersAndSaveClanmemberInformation() {
        List<Clanmember> clanmembers = getAllClanmembers().getSecond();

        if(!CollectionUtils.isEmpty(clanmembers)) {
            clanmembers.forEach(this::saveEachClanmemberInformation);
        }
    }

    public Pair<String, List<Clanmember>> getAllClanmembers() {
        ClanmembersEntity clanmembersEntity = clanmembersRepository.findFirstByOrderByIdDesc();
        if(clanmembersEntity == null) {
            return Pair.of("", new ArrayList<>());
        }

        return Pair.of(CommonsService.getDateAsString(clanmembersEntity.getId().getDate()), clanmembersEntity.getClanmembers());
    }

    private void saveEachClanmemberInformation(final Clanmember clanmember) {
        List<CSVRecord> records = connectionService.getInfoFromRunescapeForClanmember(clanmember.getName());

        if(!CollectionUtils.isEmpty(records)) {
            List<CSVRecord> levels = Lists.partition(records, 30).get(0);
            List<CSVRecord> minigames = Lists.partition(records, 30).get(1);

            clanmemberLevelsService.saveClanmemberLevelsToDatabase(clanmember.getName(), levels);
            clanmemberMinigamesService.saveClanmemberMinigamesToDatabase(clanmember.getName(), minigames);
        }
    }

    private ClanmembersEntity getClanmembersFromRunescape() {
        List<CSVRecord> records = connectionService.getInfoFromRunescapeForClan();
        records.remove(0);

        return new ClanmembersEntity(Clanmember.mapCsvRecordsToClanmembers(records));
    }
}
