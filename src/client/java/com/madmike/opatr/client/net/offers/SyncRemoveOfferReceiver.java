package com.madmike.opatr.client.net.offers;

import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

import static com.madmike.opatr.client.cache.OfferCache.OFFER_CACHE;

public class SyncRemoveOfferReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_REMOVE_OFFER_PACKET, (client, handler, buf, responseSender) -> {
            UUID offerID = buf.readUuid();

            // Run on main thread and apply to screen
            client.execute(() -> {
                OFFER_CACHE.removeIf(o -> o.offerID().equals(offerID));
            });
        });
    }
}
