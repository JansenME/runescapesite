package com.runescape.info.service;

import com.google.common.collect.Lists;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class HiScoresService {
    public List<Integer[]> getLevels(final String name) {
        return Lists.partition(getHiscores(name), 30).get(0);
    }

    public List<Integer[]> getMinigamesScores(final String name) {
        return Lists.partition(getHiscores(name), 30).get(1);
    }

    private List<Integer[]> getHiscores(final String name) {
        List<Integer[]> hiScoresLevels;

        try {
            hiScoresLevels = getHiScoresLevels(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return hiScoresLevels;
    }

    private List<Integer[]> getHiScoresLevels(final String name) throws IOException {
        String URL_HISCORE = "https://secure.runescape.com/m=hiscore/index_lite.ws?player=";

        URL url = createFullHiscoreUrl(URL_HISCORE, name);
        HttpURLConnection connection = getConnection(url);
        checkConnectionResponse(connection);

        List<Integer[]> levels = new ArrayList<>();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            levels.add(mapStringToIntegerArray(scanner.nextLine()));
        }

        scanner.close();

        return levels;
    }

    private URL createFullHiscoreUrl(final String start, final String name) throws MalformedURLException {
        return new URL(start + name);
    }

    private HttpURLConnection getConnection(final URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        return connection;
    }

    private void checkConnectionResponse (HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == 200) {
            return;
        }

        throw new RuntimeException("The connection gave back a" + connection.getResponseCode() + "response code");
    }

    private Integer[] mapStringToIntegerArray(final String value) {
        return Arrays.stream(value.split(","))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
