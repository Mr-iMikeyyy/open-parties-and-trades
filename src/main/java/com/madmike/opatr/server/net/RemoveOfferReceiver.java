package com.madmike.opatr.server.net;

import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.List;
import java.util.UUID;

public class RemoveOfferReceiver {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.REMOVE_OFFER_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // Perform cleanup or refresh on the server
                List<UUID> offers = OfferStorage.get(player.getServerWorld()).removeOffer(offer.offerID);
            });
        });
    }
}
