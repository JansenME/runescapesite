package com.maulsinc.runescape.controller;

import com.maulsinc.runescape.model.MemoryStats;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.maulsinc.runescape.CommonsService.getFormattedNumber;

@RestController
public class MemoryController {
    @GetMapping("memory-status")
    public MemoryStats getMemoryStats() {
        return new MemoryStats(
                getFormattedNumber(Runtime.getRuntime().totalMemory()),
                getFormattedNumber(Runtime.getRuntime().maxMemory()),
                getFormattedNumber(Runtime.getRuntime().freeMemory()));
    }
}
