package com.madmike.opatr.client.net;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static com.madmike.opatr.client.cache.PartyNameCache.KNOWN_PARTIES;

public class SyncUpdatePartyReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_UPDATE_PARTY_PACKET, (client, handler, buf, responseSender) -> {
            KnownParty party = KnownParty.readFromBuf(buf);

            // Run on main thread and apply to screen
            client.execute(() -> {
                KNOWN_PARTIES.put(party.partyID(), party.name());
            });
        });
    }
}
