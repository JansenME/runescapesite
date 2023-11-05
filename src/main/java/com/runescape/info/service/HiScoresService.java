package com.runescape.info.service;

import com.google.common.collect.Lists;
import com.runescape.info.model.exception.RunescapeConnectionException;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HiScoresService {
    private final ConnectionService connectionService;

    @Autowired
    public HiScoresService(final ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    public List<CSVRecord> getLevels(final String player) {
        try {
            List<CSVRecord> recordList = getHiscoresLevels(player);
            if(!recordList.isEmpty()) {
                return Lists.partition(recordList, 30).get(0);
            }
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RunescapeConnectionException(e.getMessage());
        }
    }

    private List<CSVRecord> getHiscoresLevels(final String playerName) throws IOException {
        return connectionService.getInfoFromRunescapeForPlayer(playerName);
    }
}
