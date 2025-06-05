package com.madmike.opatr.server.events;

import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.trades.TradeOffer;
import com.madmike.opatr.server.net.ServerNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class EventManager {
    public static void register() {
        // Register event to send offer sync packet when a player logs in
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            OfferStorage storage = OfferStorage.get(server);
            List<TradeOffer> offers = storage.getOffers();

            ServerNetworking.sendOfferSyncPacket(player, offers);
        });
    }
}
