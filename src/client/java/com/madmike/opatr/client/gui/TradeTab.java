package com.madmike.opatr.client.gui;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class TradeTab {
    private final String name;      // Label shown on the tab
    private final UUID partyId;     // Nullable: null for "Sell" tab

    public TradeTab(String name, @Nullable UUID partyId) {
        this.name = name;
        this.partyId = partyId;
    }

    public String getName() {
        return name;
    }

    public UUID getPartyId() {
        return partyId;
    }

    public boolean isSellTab() {
        return partyId == null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeTab tab)) return false;
        return Objects.equals(name, tab.name) && Objects.equals(partyId, tab.partyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, partyId);
    }
}
