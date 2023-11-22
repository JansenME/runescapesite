package com.maulsinc.runescape.model;

import lombok.Data;

@Data
public class MemoryStats {
    private final String heapSize;
    private final String heapMaxSize;
    private final String heapFreeSize;
}
