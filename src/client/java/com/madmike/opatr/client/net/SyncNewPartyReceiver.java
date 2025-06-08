package com.madmike.opatr.client.net;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;

import static com.madmike.opatr.client.cache.OfferCache.CLIENT_OFFERS;
import static com.madmike.opatr.client.cache.PartyNameCache.KNOWN_PARTIES;

public class SyncNewPartyReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_NEW_PARTY_PACKET, (client, handler, buf, responseSender) -> {
            KnownParty newParty = KnownParty.readFromBuf(buf);

            // Run on main thread and apply to screen
            client.execute(() -> {
                KNOWN_PARTIES.put(newParty.partyID(), newParty.name());
            });
        });
    }
}
