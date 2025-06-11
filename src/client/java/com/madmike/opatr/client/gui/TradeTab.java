package com.madmike.opatr.client.gui;

import com.madmike.opatr.server.data.TradeOffer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public record TradeTab(String name, @Nullable UUID partyId) {

}
