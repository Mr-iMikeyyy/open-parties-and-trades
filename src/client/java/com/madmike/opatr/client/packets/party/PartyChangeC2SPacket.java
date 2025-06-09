package com.madmike.opatr.client.packets.party;

import com.madmike.opatr.server.packets.PacketIDs;
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
        ClientPlayNetworking.send(PacketIDs.PARTY_CHANGE_PACKET, buf);
    }
}
