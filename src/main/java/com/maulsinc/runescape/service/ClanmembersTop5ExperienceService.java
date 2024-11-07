package com.maulsinc.runescape.service;

import com.maulsinc.runescape.configuration.ExecutionTimeLogger;
import com.maulsinc.runescape.model.Clanmember;
import com.maulsinc.runescape.model.ClanmemberLevels;
import com.maulsinc.runescape.model.Level;
import com.maulsinc.runescape.model.entity.ClanmembersTop5ExperienceEntity;
import com.maulsinc.runescape.repository.ClanmembersTop5ExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClanmembersTop5ExperienceService {
    private final ClanmembersTop5ExperienceRepository clanmembersTop5ExperienceRepository;
    private final ClanmemberLevelsService clanmemberLevelsService;

    @Autowired
    public ClanmembersTop5ExperienceService(final ClanmembersTop5ExperienceRepository clanmembersTop5ExperienceRepository,
                                            final ClanmemberLevelsService clanmemberLevelsService) {
        this.clanmembersTop5ExperienceRepository = clanmembersTop5ExperienceRepository;
        this.clanmemberLevelsService = clanmemberLevelsService;
    }

    public void saveClanmembersTop5Experience(final List<Clanmember> clanmembers) {
        List<ClanmemberLevels> clanmemberLevels = new ArrayList<>(clanmembers.stream()
                .map(clanmemberLevel -> clanmemberLevelsService.getOneClanmemberLevels(clanmemberLevel.getName()))
                .toList());

        fixNullValues(clanmemberLevels);

        clanmemberLevels.sort((level1, level2) -> Math.toIntExact(level2.getLevels().get(0).getExperienceToday() - level1.getLevels().get(0).getExperienceToday()));

        List<ClanmemberLevels> listToSave = clanmemberLevels.stream()
                .limit(5)
                .filter(clanmemberLevel -> clanmemberLevel.getLevels().get(0).getExperienceToday() > 0)
                .toList();

        clanmembersTop5ExperienceRepository.save(getClanmembersTop5ExperienceEntity(listToSave));
    }

    @ExecutionTimeLogger
    public List<ClanmemberLevels> getClanmembersTop5Experience() {
        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = clanmembersTop5ExperienceRepository.findFirstByOrderByIdDesc();

        if (clanmembersTop5ExperienceEntity != null) {
            return clanmembersTop5ExperienceRepository.findFirstByOrderByIdDesc().getClanmemberLevels();
        }

        return new ArrayList<>();
    }

    private void fixNullValues(final List<ClanmemberLevels> clanmemberLevels) {
        clanmemberLevels.forEach(clanmemberLevel -> {
            if (clanmemberLevel.getLevels() == null) {
                Level level = new Level();

                level.setExperienceToday(0L);

                clanmemberLevel.setLevels(List.of(level));
            }

            if (clanmemberLevel.getLevels().get(0).getExperienceToday() == null) {
                clanmemberLevel.getLevels().get(0).setExperienceToday(0L);
            }
        });
    }

    private ClanmembersTop5ExperienceEntity getClanmembersTop5ExperienceEntity(final List<ClanmemberLevels> listToSave) {
        ClanmembersTop5ExperienceEntity clanmembersTop5ExperienceEntity = new ClanmembersTop5ExperienceEntity();

        clanmembersTop5ExperienceEntity.setClanmemberLevels(listToSave);

        return clanmembersTop5ExperienceEntity;
    }
}