package com.runescape.info.service;

import com.runescape.info.model.entity.ClanmembersEntity;
import com.runescape.info.model.Clanmember;
import com.runescape.info.model.exception.RunescapeConnectionException;
import com.runescape.info.repository.ClanmembersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ClanmembersService {
    private final ConnectionService connectionService;
    private final ClanmembersRepository clanmembersRepository;

    @Autowired
    public ClanmembersService(final ConnectionService connectionService, final ClanmembersRepository clanmembersRepository) {
        this.connectionService = connectionService;
        this.clanmembersRepository = clanmembersRepository;
    }

    public Pair<String, List<Clanmember>> getAllClanmembers() {
        ClanmembersEntity clanmembersEntity = clanmembersRepository.findFirstByOrderByIdDesc();
        if(clanmembersEntity == null) {
            return Pair.of("", new ArrayList<>());
        }

        return Pair.of(getDateAsString(clanmembersEntity), clanmembersRepository.findFirstByOrderByIdDesc().getClanmembers());
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void saveAllPlayersFromRunescape() {
        try {
            ClanmembersEntity entity = clanmembersRepository.save(getPlayersFromRunescape("Mauls Inc"));
            log.info("Saved " + entity.getClanmembers().size() + " clanmembers from Mauls Inc to database.");
        } catch (IOException e) {
            throw new RunescapeConnectionException(e.getMessage());
        }
    }

    private String getDateAsString(ClanmembersEntity clanmembersEntity) {
        Date date = clanmembersEntity.getId().getDate();

        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy h:mm a z");
        return format.format(date);
    }

    private ClanmembersEntity getPlayersFromRunescape(final String clanName) throws IOException {
        List<CSVRecord> records = connectionService.getInfoFromRunescapeForClan(clanName);
        records.remove(0);

        return new ClanmembersEntity(Clanmember.mapCsvRecordsToClanmembers(records));
    }
}
