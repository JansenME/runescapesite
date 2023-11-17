package com.maulsinc.runescape.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maulsinc.runescape.model.exception.RunescapeConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConnectionService {
    private static final String WEB_SERVICE_URL = "http://secure.runescape.com";
    private static final String CLANMEMBER_INFORMATION_URL = WEB_SERVICE_URL + "/m=hiscore/index_lite.ws?player=";
    private static final String CLAN_INFORMATION_URL = WEB_SERVICE_URL + "/m=clan-hiscores/members_lite.ws?clanName=";

    private static final String WEB_SERVICE_RUNEMETRICS_URL = "http://apps.runescape.com/runemetrics";
    private static final String CLANMEMBER_QUESTS_URL = WEB_SERVICE_RUNEMETRICS_URL + "/quests?user=";
    private static final String CLANMEMBER_PROFILE_URL = WEB_SERVICE_RUNEMETRICS_URL + "/profile/profile?user=";

    private static final String CLAN_NAME = "Mauls Inc";

    public List<CSVRecord> getCSVRecordsFromRunescapeForClan() {
        return getCSVInfoFromRunescape(CLAN_INFORMATION_URL + replaceEmptySpace(CLAN_NAME));
    }

    public List<CSVRecord> getCSVRecordsFromRunescapeForClanmember(final String clanmember) {
        return getCSVInfoFromRunescape(CLANMEMBER_INFORMATION_URL + replaceEmptySpace(clanmember));
    }

    public JsonNode getJsonNodeFromRunescapeForClanmemberQuests(final String clanmember) {
        return getJsonNodeInfoFromRunescape(CLANMEMBER_QUESTS_URL + replaceEmptySpace(clanmember));
    }

    public JsonNode getJsonNodeFromRunescapeForClanmemberProfile(final String clanmember) {
        return getJsonNodeInfoFromRunescape(CLANMEMBER_PROFILE_URL + replaceEmptySpace(clanmember));
    }

    private List<CSVRecord> getCSVInfoFromRunescape(final String url) {
        try {
            CSVParser parser = CSVParser.parse(getHttpEntityAsString(url), CSVFormat.Builder.create().setDelimiter(",").build());
            return parser.getRecords();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private JsonNode getJsonNodeInfoFromRunescape(final String url) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readTree(getHttpEntityAsString(url));
        } catch (JsonProcessingException e) {
            return objectMapper.nullNode();
        }
    }

    private String getHttpEntityAsString(final String url) {
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("accept", "application/json");
        request.addHeader("accept", "text/csv");

        try (CloseableHttpClient client = HttpClients.createDefault();
             CloseableHttpResponse response = client.execute(request)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                log.info(String.format("The url %s was called, but returned a %s response code", url, response.getStatusLine().getStatusCode()));
                return "";
            }

            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RunescapeConnectionException(e.getMessage());
        }
    }

    private String replaceEmptySpace(String name) {
        name = name.replace(" ", "+");
        name = name.replace(" ", "+");
        return name;
    }
}
