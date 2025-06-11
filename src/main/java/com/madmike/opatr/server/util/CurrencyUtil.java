package com.madmike.opatr.server.util;

public class CurrencyUtil {
    public static final int BRONZE_PER_SILVER = 100;
    public static final int SILVER_PER_GOLD = 100;
    public static final int BRONZE_PER_GOLD = BRONZE_PER_SILVER * SILVER_PER_GOLD;

    // Converts gold, silver, bronze to total bronze
    public static long toTotalBronze(int gold, int silver, int bronze) {
        return (long) gold * BRONZE_PER_GOLD + (long) silver * BRONZE_PER_SILVER + bronze;
    }

    // Converts total bronze to gold/silver/bronze
    public static CoinBreakdown fromTotalBronze(long totalBronze) {
        int gold = (int) (totalBronze / BRONZE_PER_GOLD);
        totalBronze %= BRONZE_PER_GOLD;

        int silver = (int) (totalBronze / BRONZE_PER_SILVER);
        int bronze = (int) (totalBronze % BRONZE_PER_SILVER);

        return new CoinBreakdown(gold, silver, bronze);
    }

    // Simple record to hold breakdown
    public record CoinBreakdown(int gold, int silver, int bronze) {}
}
