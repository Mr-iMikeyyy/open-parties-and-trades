package com.madmike.opatr.client.net.party;

import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ListIterator;
import java.util.UUID;

import static com.madmike.opatr.client.cache.OfferCache.CLIENT_OFFERS;

public class SyncPlayerPartyChangeReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_PLAYER_PARTY_CHANGE_PACKET, (client, handler, buf, responseSender) -> {
            UUID playerId = buf.readUuid();
            UUID partyId = buf.readBoolean() ? buf.readUuid() : null;

            client.execute(() -> {
                ListIterator<TradeOffer> iterator = CLIENT_OFFERS.listIterator();
                while (iterator.hasNext()) {
                    TradeOffer offer = iterator.next();
                    if (playerId.equals(offer.sellerID())) {
                        TradeOffer updated = new TradeOffer(
                                offer.offerID(),
                                offer.sellerID(),
                                offer.item().copy(),
                                offer.price(),
                                partyId
                        );
                        iterator.set(updated);
                    }
                }
            });
        });
    }
}
