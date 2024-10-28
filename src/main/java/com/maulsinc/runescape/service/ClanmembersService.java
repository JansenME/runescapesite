package com.maulsinc.runescape.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.common.collect.Lists;
import com.maulsinc.runescape.CommonsService;
import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.exception.ExecutionException;
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
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@Slf4j
public class ClanmembersService {
    private final ConnectionService connectionService;
    private final ClanmemberLevelsService clanmemberLevelsService;
    private final ClanmemberMinigamesService clanmemberMinigamesService;
    private final ClanmemberQuestsService clanmemberQuestsService;
    private final ClanmembersRepository clanmembersRepository;

    @Autowired
    public ClanmembersService(final ConnectionService connectionService,
                              final ClanmemberLevelsService clanmemberLevelsService,
                              final ClanmemberMinigamesService clanmemberMinigamesService,
                              final ClanmemberQuestsService clanmemberQuestsService,
                              final ClanmembersRepository clanmembersRepository) {
        this.connectionService = connectionService;
        this.clanmemberLevelsService = clanmemberLevelsService;
        this.clanmemberMinigamesService = clanmemberMinigamesService;
        this.clanmemberQuestsService = clanmemberQuestsService;
        this.clanmembersRepository = clanmembersRepository;
    }

    //@Scheduled(cron = "0 0 10 * * *")
    @ExecutionTimeLogger
    public void saveAllClanmembersFromRunescape() {
        ClanmembersEntity clanmembersEntity = getClanmembersFromRunescape();

        if(!CollectionUtils.isEmpty(clanmembersEntity.getClanmembers())) {
            ClanmembersEntity entity = clanmembersRepository.save(clanmembersEntity);

            String amount = "unknown amount of";

            if(entity != null) {
                amount = String.valueOf(entity.getClanmembers().size());
            }

            log.info("Saved {} clanmembers from Mauls Inc to database.", amount);
        }
    }

    //@Scheduled(cron = "0 */20 * * * *")
    @ExecutionTimeLogger
    public void getAllClanmembersAndSaveClanmemberInformation() {
        List<Clanmember> clanmembers = getAllClanmembers().getSecond();

        if(!CollectionUtils.isEmpty(clanmembers)) {
            List<Clanmember> unique = clanmembers.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Clanmember::getName))),
                            ArrayList::new));

            handleExecutorService(unique);
        }
    }

    //@Scheduled(cron = "0 30 */4 * * *")
    @ExecutionTimeLogger
    public void getAllClanmembersAndSaveClanmemberQuests() {
        List<Clanmember> clanmembers = getAllClanmembers().getSecond();

        if(!CollectionUtils.isEmpty(clanmembers)) {
            clanmembers.forEach(this::checkIfSaveEachClanmemberQuests);
        }
    }

    public Pair<String, List<Clanmember>> getAllClanmembers() {
        ClanmembersEntity clanmembersEntity = clanmembersRepository.findFirstByOrderByIdDesc();
        if(clanmembersEntity == null) {
            return Pair.of("", new ArrayList<>());
        }

        return Pair.of(CommonsService.getDateAsString(clanmembersEntity.getId().getDate()), clanmembersEntity.getClanmembers());
    }

    private ClanmembersEntity getClanmembersFromRunescape() {
        List<CSVRecord> records = connectionService.getCSVRecordsFromRunescapeForClan();

        if(!CollectionUtils.isEmpty(records)) {
            records.remove(0);
        }

        List<Clanmember> clanmembers = Clanmember.mapCsvRecordsToClanmembers(records);

        setIronmanBooleans(clanmembers);

        return new ClanmembersEntity(clanmembers);
    }

    private void setIronmanBooleans(final List<Clanmember> clanmembers) {
        clanmembers.forEach(this::setCorrectBooleans);
    }

    private void setCorrectBooleans(Clanmember clanmember) {
        setCorrectValueForIronman(clanmember);
        setCorrectValueForHardcoreIronman(clanmember);
    }

    private void setCorrectValueForIronman(Clanmember clanmember) {
        List<CSVRecord> records = connectionService.getCSVRecordsFromRunescapeForClanmemberIronman(clanmember.getName());

        if(!records.isEmpty()) {
            clanmember.setIronman(true);
        }
    }

    private void setCorrectValueForHardcoreIronman(Clanmember clanmember) {
        List<CSVRecord> records = connectionService.getCSVRecordsFromRunescapeForClanmemberHardcoreIronman(clanmember.getName());

        if(!records.isEmpty()) {
            clanmember.setHardcoreIronman(true);
        }
    }

    private void handleExecutorService(final List<Clanmember> clanmembers) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<String>> callables = clanmembers.stream()
                .<Callable<String>>map(clanmember -> () -> checkIfSaveEachClanmemberInformation(clanmember))
                .toList();

        try {
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            log.error("Something went wrong with the executor service");
            Thread.currentThread().interrupt();
            throw new ExecutionException("The execution failed.", e);
        } finally {
            executorService.shutdown();
            log.info("ExecutorService was successfully shutdown.");
        }
    }

    private String checkIfSaveEachClanmemberInformation(final Clanmember clanmember) {
        JsonNode jsonNode = connectionService.getJsonNodeFromRunescapeForClanmemberProfile(clanmember.getName());
        List<CSVRecord> records = connectionService.getCSVRecordsFromRunescapeForClanmember(clanmember.getName());

        if(CollectionUtils.isEmpty(records)) {
            return "FAILED";
        }

        if(records.size() != 60) {
            log.error("The list from Runescape was not the correct size of 60, but it was {}", records.size());
            return "FAILED";
        }

        List<List<CSVRecord>> levelsAndMinigames = Lists.partition(records, 30);

        saveEachClanmemberLevels(levelsAndMinigames.get(0), jsonNode, clanmember);
        saveEachClanmemberMinigames(levelsAndMinigames.get(1), clanmember);

        return "SUCCESS";
    }

    private void saveEachClanmemberLevels(final List<CSVRecord> levels, final JsonNode jsonNode, final Clanmember clanmember) {
        if(jsonNode.has("error")) {
            clanmemberLevelsService.saveClanmemberLevelsToDatabase(clanmember.getName(), levels);
        } else if (jsonNode.has("skillvalues")) {
            clanmemberLevelsService.saveClanmemberLevelsToDatabaseFromProfile(clanmember.getName(), jsonNode);
        }
    }

    private void saveEachClanmemberMinigames(final List<CSVRecord> minigames, final Clanmember clanmember) {
        clanmemberMinigamesService.saveClanmemberMinigamesToDatabase(clanmember.getName(), minigames);
    }

    private void checkIfSaveEachClanmemberQuests(final Clanmember clanmember) {
        JsonNode jsonNode = connectionService.getJsonNodeFromRunescapeForClanmemberQuests(clanmember.getName());

        if(jsonNode instanceof NullNode) {
            log.info("Getting quests for {} failed, the JSON received was empty.", clanmember.getName());
            return;
        }

        saveEachClanmemberQuests(jsonNode.get("quests"), clanmember.getName());
    }

    private void saveEachClanmemberQuests(final JsonNode jsonNodeQuests, final String clanmember) {
        clanmemberQuestsService.saveClanmemberQuestsToDatabase(clanmember, jsonNodeQuests);
    }
}
