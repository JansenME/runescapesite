package com.runescape.info.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Martijn Jansen on 6/10/2017.
 */
@Entity
public class ExperienceNormalSkills {
    private Long id;
    private Long totalExperience;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Long totalExperience) {
        this.totalExperience = totalExperience;
    }
}
