package com.madmike.opatr.client.net.offers;

import com.madmike.opatr.server.packets.PacketIDs;
import com.madmike.opatr.server.data.TradeOffer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;

import static com.madmike.opatr.client.cache.OfferCache.CLIENT_OFFERS;

public class SyncAllOffersReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_ALL_OFFERS_PACKET, (client, handler, buf, responseSender) -> {
            List<TradeOffer> offers = new ArrayList<>();
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                offers.add(TradeOffer.readFromBuf(buf));
            }

            // Run on main thread and apply to screen
            client.execute(() -> {
                CLIENT_OFFERS.clear();
                CLIENT_OFFERS.addAll(offers);
            });
        });
    }
}
