package com.runescape.info.service;

import com.runescape.info.model.entity.ClanmembersEntity;
import com.runescape.info.model.Clanmember;
import com.runescape.info.model.exception.MemberNotFoundException;
import com.runescape.info.model.exception.RunescapeConnectionException;
import com.runescape.info.repository.ClanmembersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        return clanmembersRepository.findFirstByOrderByIdDesc().getClanmembers();
    }

    public Clanmember findCorrectClanmember(final String player) {
        for(Clanmember clanmember : getAllClanmembers()) {
            if(player.equalsIgnoreCase(clanmember.getName())) {
                return clanmember;
            }
        }

        throw new MemberNotFoundException("The member (" + player + ") was not found in the database.");
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
