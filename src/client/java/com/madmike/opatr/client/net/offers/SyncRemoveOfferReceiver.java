package com.madmike.opatr.client.net.offers;

import com.madmike.opatr.client.gui.TradingScreen;
import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

import static com.madmike.opatr.client.cache.OfferCache.OFFER_CACHE;

public class SyncRemoveOfferReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_REMOVE_OFFER_PACKET, (client, handler, buf, responseSender) -> {
            TradeOffer offer = TradeOffer.readFromBuf(buf);

            // Run on main thread and apply to screen
            client.execute(() -> {
                OFFER_CACHE.removeIf(o -> o.offerID().equals(offer.offerID()));
                if (client.currentScreen instanceof TradingScreen tradingScreen) {
                    if (tradingScreen.getCurrentTab().partyId().equals(offer.partyID())) {
                        tradingScreen.refresh();
                    }
                }
            });
        });
    }
}
