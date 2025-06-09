package com.madmike.opatr.server.events;

import com.madmike.opatr.server.data.TradeOfferStorage;
import com.madmike.opatr.server.monitor.PartyNameMonitor;
import com.madmike.opatr.server.packets.offers.SyncAllOffersS2CPacket;
import com.madmike.opatr.server.packets.party.SyncAllPartiesS2CPacket;
import com.madmike.opatr.server.data.TradeOffer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EventManager {
    public static void register() {
        // Register event to send offer sync packet when a player logs in
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            // Sync offers
            TradeOfferStorage storage = TradeOfferStorage.get(player.getServerWorld());
            List<TradeOffer> offers = storage.getOffers();
            SyncAllOffersS2CPacket.send(player, offers);

            // Sync party ID -> name map
            HashMap<UUID, String> partyIdNameMap = new HashMap<>();
            OpenPACServerAPI.get(server).getPartyManager().getAllStream().forEach(party -> {
                partyIdNameMap.put(party.getId(), party.getDefaultName());
            });

            SyncAllPartiesS2CPacket.send(player, partyIdNameMap);
        });

        //Start the party name monitor
        ServerLifecycleEvents.SERVER_STARTED.register(PartyNameMonitor::register);
    }
}
