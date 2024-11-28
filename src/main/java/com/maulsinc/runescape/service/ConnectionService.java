package com.maulsinc.runescape.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maulsinc.runescape.model.exception.RunescapeConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
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
    private static final String CLANMEMBER_IRONMAN_INFORMATION_URL = WEB_SERVICE_URL + "/m=hiscore_ironman/index_lite.ws?player=";
    private static final String CLANMEMBER_HARDCORE_IRONMAN_INFORMATION_URL = WEB_SERVICE_URL + "/m=hiscore_hardcore_ironman/index_lite.ws?player=";
    private static final String CLAN_INFORMATION_URL = WEB_SERVICE_URL + "/m=clan-hiscores/members_lite.ws?clanName=";

    private static final String WEB_SERVICE_RUNEMETRICS_URL = "http://apps.runescape.com/runemetrics";
    private static final String CLANMEMBER_QUESTS_URL = WEB_SERVICE_RUNEMETRICS_URL + "/quests?user=";
    private static final String CLANMEMBER_PROFILE_URL = WEB_SERVICE_RUNEMETRICS_URL + "/profile/profile?user=";

    private static final String CLAN_NAME = "Mauls Inc";

    private static final int MAX_RETRIES = 10;
    private static final List<Integer> RETRYABLE_ERROR_CODES = List.of(500, 501, 502, 503, 504);

    public List<CSVRecord> getCSVRecordsFromRunescapeForClan() {
        return getCSVInfoFromRunescape(CLAN_INFORMATION_URL + replaceEmptySpace(CLAN_NAME));
    }

    public List<CSVRecord> getCSVRecordsFromRunescapeForClanmember(final String clanmember) {
        return getCSVInfoFromRunescape(CLANMEMBER_INFORMATION_URL + replaceEmptySpace(clanmember));
    }

    public List<CSVRecord> getCSVRecordsFromRunescapeForClanmemberIronman(final String clanmember) {
        return getCSVInfoFromRunescape(CLANMEMBER_IRONMAN_INFORMATION_URL + replaceEmptySpace(clanmember));
    }

    public List<CSVRecord> getCSVRecordsFromRunescapeForClanmemberHardcoreIronman(final String clanmember) {
        return getCSVInfoFromRunescape(CLANMEMBER_HARDCORE_IRONMAN_INFORMATION_URL + replaceEmptySpace(clanmember));
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
        HttpUriRequest request = createRequest(url);

        try (CloseableHttpClient client = createClient();
             CloseableHttpResponse response = client.execute(request)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                log.debug(String.format("The url %s was called, but returned a %s response code", url, response.getStatusLine().getStatusCode()));
                return "";
            }

            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RunescapeConnectionException(e.getMessage());
        }
    }

    private HttpUriRequest createRequest(final String url) {
        HttpUriRequest request = new HttpGet(url);
        request.addHeader("accept", "application/json");
        request.addHeader("accept", "text/csv");

        return request;
    }

    private CloseableHttpClient createClient() {
        return HttpClientBuilder.create()
                .setServiceUnavailableRetryStrategy(createServiceUnavailableRetryStrategy())
                .build();
    }

    private ServiceUnavailableRetryStrategy createServiceUnavailableRetryStrategy() {
        return new ServiceUnavailableRetryStrategy() {
            @Override
            public boolean retryRequest(HttpResponse httpResponse, int executionCount, HttpContext httpContext) {
                if(RETRYABLE_ERROR_CODES.contains(httpResponse.getStatusLine().getStatusCode()) && executionCount >= MAX_RETRIES) {
                    log.info(String.format("This is try number %s. The url %s came back with a %s response", executionCount, ((HttpClientContext) httpContext).getRequest().getRequestLine().getUri(), httpResponse.getStatusLine().getStatusCode()));
                    return true;
                }

                return false;
            }

            @Override
            public long getRetryInterval() {
                return 250;
            }
        };
    }

    private String replaceEmptySpace(String name) {
        name = name.replace(" ", "+");
        name = name.replace(" ", "+");
        return name;
    }
}
