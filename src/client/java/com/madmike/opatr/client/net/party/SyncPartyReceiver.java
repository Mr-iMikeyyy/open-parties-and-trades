package com.madmike.opatr.client.net.party;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static com.madmike.opatr.client.cache.PartyNameCache.PARTY_NAME_CACHE;

public class SyncPartyReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_PARTY_PACKET, (client, handler, buf, responseSender) -> {
            KnownParty party = KnownParty.readFromBuf(buf);

            // Run on main thread and apply to screen
            client.execute(() -> {
                PARTY_NAME_CACHE.put(party.partyID(), party.name());
            });
        });
    }
}
