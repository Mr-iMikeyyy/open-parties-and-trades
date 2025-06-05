package com.madmike.opatr.client.gui;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public record TradeTab(String name, UUID partyId) {
    public TradeTab(String name, @Nullable UUID partyId) {
        this.name = name;
        this.partyId = partyId;
    }
}
