package com.madmike.opatr.server.net;

import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import com.madmike.opatr.server.packets.offers.SyncRemoveOfferS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class RemoveOfferReceiver {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.REMOVE_OFFER_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {

                OfferStorage storage = OfferStorage.get(player.getServerWorld());
                TradeOffer offer = TradeOffer.readFromBuf(buf);
                storage.removeOffer(offer);
                SyncRemoveOfferS2CPacket.sendToAll(offer, server);
            });
        });
    }
}
