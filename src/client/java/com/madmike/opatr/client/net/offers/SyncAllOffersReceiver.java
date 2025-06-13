package com.madmike.opatr.client.net.offers;

import com.madmike.opatr.client.gui.TradingScreen;
import com.madmike.opatr.server.packets.PacketIDs;
import com.madmike.opatr.server.data.TradeOffer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.madmike.opatr.client.cache.OfferCache.OFFER_CACHE;

public class SyncAllOffersReceiver {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_ALL_OFFERS_PACKET, (client, handler, buf, responseSender) -> {
            CompletableFuture.runAsync(() -> {
                List<TradeOffer> offers = new ArrayList<>();
                int count = buf.readInt();
                for (int i = 0; i < count; i++) {
                    offers.add(TradeOffer.readFromBuf(buf));
                }

                // Run on main thread and apply to screen
                client.execute(() -> {
                    OFFER_CACHE.clear();
                    OFFER_CACHE.addAll(offers);
                    if (client.currentScreen instanceof TradingScreen tradingScreen) {
                        tradingScreen.refresh();
                    }
                });
            });
        });
    }
}
