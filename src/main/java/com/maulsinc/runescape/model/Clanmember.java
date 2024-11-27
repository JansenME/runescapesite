package com.maulsinc.runescape.model;

import com.maulsinc.runescape.CommonsService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Clanmember implements Comparable<Clanmember> {
    private String name;
    private Rank rank;
    private Long totalXP;
    private Long kills;
    private boolean ironman;
    private boolean hardcoreIronman;

    public String getFormattedXp() {
        return CommonsService.getFormattedNumber(this.totalXP);
    }

    public String getFormattedKills() {
        return CommonsService.getFormattedNumber(this.kills);
    }

    public static List<Clanmember> mapCsvRecordsToClanmembers(final List<CSVRecord> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        return records.stream()
                .map(Clanmember::mapOneCsvRecordToClanmember)
                .toList();
    }

    private static Clanmember mapOneCsvRecordToClanmember(final CSVRecord csvRecord) {
        Clanmember clanmember = new Clanmember();

        clanmember.setName(csvRecord.get(0));
        clanmember.setRank(Rank.getEnumWithName(csvRecord.get(1)));
        clanmember.setTotalXP(Long.valueOf(csvRecord.get(2)));
        clanmember.setKills(Long.valueOf(csvRecord.get(3)));

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

    @Override
    public int compareTo(Clanmember o) {
        return rank.getOrder() - o.rank.getOrder();
    }
}
