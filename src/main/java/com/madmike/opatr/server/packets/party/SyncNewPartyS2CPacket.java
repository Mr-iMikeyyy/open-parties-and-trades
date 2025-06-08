package com.madmike.opatr.server.packets.party;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncNewPartyS2CPacket {
    public static void sendToAll(MinecraftServer server, KnownParty party) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(party.partyID());
        buf.writeString(party.name());

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, PacketIDs.SYNC_NEW_PARTY_PACKET, buf);
        }
    }
}
