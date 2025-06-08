package com.madmike.opatr.client.net;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.List;
import java.util.UUID;

import static com.madmike.opatr.client.cache.OfferCache.CLIENT_OFFERS;
import static com.madmike.opatr.client.cache.PartyNameCache.KNOWN_PARTIES;

public class SyncRemoveOfferReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_REMOVE_OFFER_PACKET, (client, handler, buf, responseSender) -> {
            UUID offerID = buf.readUuid();

            // Run on main thread and apply to screen
            client.execute(() -> {
                CLIENT_OFFERS.removeIf(o -> o.offerID().equals(offerID));
            });
        });
    }
}
