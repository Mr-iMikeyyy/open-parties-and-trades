package com.madmike.opatr.server.net;

import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.packets.PacketIDs;
import com.madmike.opatr.server.packets.party.SyncPartyChangeS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.UUID;

public class PartyChangeReceiver {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.PARTY_CHANGE_PACKET, (server, player, handler, buf, responseSender) -> {
            UUID playerId = buf.readUuid();
            boolean hasParty = buf.readBoolean();
            UUID partyId = hasParty ? buf.readUuid() : null;

            server.execute(() -> {
                // Handle the player's party change

                OfferStorage storage = OfferStorage.get(player.getServerWorld());

                storage.updatePlayerOffers(playerId, partyId);

                SyncPartyChangeS2CPacket.sendToAll(playerId, partyId, server);
            });
        });
    }
}
