package com.runescape.info.service;

import com.runescape.info.model.entity.ClanmembersEntity;
import com.runescape.info.model.Clanmember;
import com.runescape.info.model.exception.MemberNotFoundException;
import com.runescape.info.model.exception.RunescapeConnectionException;
import com.runescape.info.repository.ClanmembersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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

    public List<Clanmember> getAllClanmembers() {
        ClanmembersEntity clanmembersEntity = clanmembersRepository.findFirstByOrderByIdDesc();
        if(clanmembersEntity == null) {
            return new ArrayList<>();
        }
        return clanmembersRepository.findFirstByOrderByIdDesc().getClanmembers();
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

    private ClanmembersEntity getPlayersFromRunescape(final String clanName) throws IOException {
        List<CSVRecord> records = connectionService.getInfoFromRunescapeForClan(clanName);
        records.remove(0);

        return new ClanmembersEntity(Clanmember.mapCsvRecordsToClanmembers(records));
    }
}
