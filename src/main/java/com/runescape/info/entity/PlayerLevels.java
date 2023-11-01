package com.runescape.info.entity;

import com.runescape.info.model.Level;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlayerLevels {
    @Id
    private ObjectId id;

    private String player;
    private String date;
    private List<Level> levels;
}
