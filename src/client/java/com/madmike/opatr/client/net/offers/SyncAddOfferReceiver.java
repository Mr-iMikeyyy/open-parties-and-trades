package com.madmike.opatr.client.net.offers;

import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static com.madmike.opatr.client.cache.OfferCache.OFFER_CACHE;

public class SyncAddOfferReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_ADD_OFFER_PACKET, (client, handler, buf, responseSender) -> {
            TradeOffer offer = TradeOffer.readFromBuf(buf);

            // Run on main thread and apply to screen
            client.execute(() -> {
                OFFER_CACHE.add(offer);
            });
        });
    }
}
