package com.runescape.info.service;

import com.runescape.info.model.exception.RunescapeConnectionException;
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

@Service
public class ConnectionService {
    private static final String WEB_SERVICE_URL = "http://secure.runescape.com";
    private static final String CLANMEMBER_INFORMATION_URL = WEB_SERVICE_URL + "/m=hiscore/index_lite.ws?player=";
    private static final String CLAN_INFORMATION_URL = WEB_SERVICE_URL + "/m=clan-hiscores/members_lite.ws?clanName=";

    public List<CSVRecord> getInfoFromRunescapeForClan(final String clanName) {
        try {
            return getInfoFromRunescape(CLAN_INFORMATION_URL + replaceEmptySpace(clanName));
        } catch (IOException e) {
            throw new RunescapeConnectionException(e.getMessage());
        }
    }

    public List<CSVRecord> getInfoFromRunescapeForClanmember(final String clanmember) {
        try {
            return getInfoFromRunescape(CLANMEMBER_INFORMATION_URL + replaceEmptySpace(clanmember));
        } catch (IOException e) {
            throw new RunescapeConnectionException(e.getMessage());
        }
    }

    private List<CSVRecord> getInfoFromRunescape(final String url) throws IOException {
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("accept", "application/json");
        request.addHeader("accept", "text/csv");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 404) {
                return new ArrayList<>();
            }

            String responseEntity = EntityUtils.toString(response.getEntity());
            CSVParser parser = CSVParser.parse(responseEntity, CSVFormat.Builder.create().setDelimiter(",").build());
            return parser.getRecords();
        }
    }

    private String replaceEmptySpace(String name) {
        name = name.replace(" ", "+");
        name = name.replace(" ", "+");
        return name;
    }
}
