package com.madmike.opatr.client.packets;

import com.madmike.opatr.server.net.ServerNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PartyChangeC2SPacket {
    public static void send(UUID playerID, @Nullable UUID partyID) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(playerID);
        buf.writeBoolean(partyID != null); // Write presence flag
        if (partyID != null) {
            buf.writeUuid(partyID);
        }
        ClientPlayNetworking.send(ServerNetworking.PARTY_CHANGE_PACKET, PacketByteBufs.empty());
    }
}
