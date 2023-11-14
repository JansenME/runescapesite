package com.runescape.info.model;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ClanmemberTest {

    @Test
    void testGetFormattedXp() {
        Clanmember clanmember = new Clanmember();
        clanmember.setTotalXP(5000000L);

        assertEquals("5.000.000", clanmember.getFormattedXp());
    }

    @Test
    void testGetFormattedXpNull() {
        Clanmember clanmember = new Clanmember();
        clanmember.setTotalXP(null);

        assertEquals("--", clanmember.getFormattedXp());
    }

    @Test
    void testGetFormattedKills() {
        Clanmember clanmember = new Clanmember();
        clanmember.setKills(5000000L);

        assertEquals("5.000.000", clanmember.getFormattedKills());
    }

    @Test
    void testGetFormattedKillsNull() {
        Clanmember clanmember = new Clanmember();
        clanmember.setTotalXP(null);

        assertEquals("--", clanmember.getFormattedKills());
    }

    @Test
    void testMapCsvRecordsToClanmembers() {
        List<Clanmember> clanmembers = Clanmember.mapCsvRecordsToClanmembers(getCsvRecords());

        assertEquals("Name", clanmembers.get(0).getName());
        assertEquals(Rank.OWNER, clanmembers.get(0).getRank());
        assertEquals(5000000L, clanmembers.get(0).getTotalXP());
        assertEquals(120L, clanmembers.get(0).getKills());

        assertEquals("Also a name", clanmembers.get(1).getName());
        assertEquals(Rank.OVERSEER, clanmembers.get(1).getRank());
        assertEquals(2500000000L, clanmembers.get(1).getTotalXP());
        assertEquals(4L, clanmembers.get(1).getKills());
    }

    private List<CSVRecord> getCsvRecords() {
        CSVRecord csvRecord = Mockito.mock(CSVRecord.class);
        when(csvRecord.get(0)).thenReturn("Name");
        when(csvRecord.get(1)).thenReturn("Owner");
        when(csvRecord.get(2)).thenReturn("5000000");
        when(csvRecord.get(3)).thenReturn("120");

        CSVRecord csvRecord2 = Mockito.mock(CSVRecord.class);
        when(csvRecord2.get(0)).thenReturn("Also a name");
        when(csvRecord2.get(1)).thenReturn("Overseer");
        when(csvRecord2.get(2)).thenReturn("2500000000");
        when(csvRecord2.get(3)).thenReturn("4");

        return new ArrayList<>(
                Arrays.asList(csvRecord, csvRecord2)
        );
    }
}