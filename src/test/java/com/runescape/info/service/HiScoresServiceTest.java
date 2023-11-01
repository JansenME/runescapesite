package com.runescape.info.service;

import org.junit.Before;
import org.junit.Test;

public class HiScoresServiceTest {
    private static HiScoresService hiScoresService;

    @Before
    public void setUp() {
        hiScoresService = new HiScoresService();
    }

    @Test
    public void test () {
        hiScoresService.getLevels("HC_Kloeperd");
    }

}