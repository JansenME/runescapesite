package com.runescape.info.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Clanmember {
    private String name;
    private Rank rank;
    private Long totalXP;
    private Long kills;

    public static List<Clanmember> mapCsvRecordsToClanmembers(final List<CSVRecord> records) {
        return records.stream()
                .map(Clanmember::mapOneCsvRecordToClanmember)
                .toList();
    }

    private static Clanmember mapOneCsvRecordToClanmember(final CSVRecord record) {
        Clanmember clanmember = new Clanmember();

        clanmember.setName(record.get(0));
        clanmember.setRank(Rank.getEnumWithName(record.get(1)));
        clanmember.setTotalXP(Long.valueOf(record.get(2)));
        clanmember.setKills(Long.valueOf(record.get(3)));

        return clanmember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clanmember that = (Clanmember) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rank, totalXP, kills);
    }
}
