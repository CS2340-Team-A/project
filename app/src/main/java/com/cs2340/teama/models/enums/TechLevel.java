package com.cs2340.teama.models.enums;

/**
 *
 */
public enum TechLevel {
    PreAgriculture(0),
    Agriculture(1),
    Medieval(2),
    Renaissance(3),
    EarlyIndustrial(4),
    Industrial(5),
    PostIndustrial(6),
    HiTech(7);

    private final int techLv;

    TechLevel(int lv) {
        techLv = lv;
    }

    /**
     * @return associated tech level
     */
    public int getTechLv() {
        return techLv;
    }
}
