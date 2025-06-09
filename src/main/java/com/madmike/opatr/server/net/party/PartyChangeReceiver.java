package com.madmike.opatr.server.net.party;

import com.madmike.opatr.server.data.TradeOfferStorage;
import com.madmike.opatr.server.packets.PacketIDs;
import com.madmike.opatr.server.packets.party.SyncPlayerPartyChangeS2CPacket;
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

                TradeOfferStorage storage = TradeOfferStorage.get(player.getServerWorld());

                storage.updatePlayerOffers(playerId, partyId);

                SyncPlayerPartyChangeS2CPacket.sendToAll(playerId, partyId, server);
            });
        });
    }
}
