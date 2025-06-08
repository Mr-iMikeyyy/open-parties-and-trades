package com.madmike.opatr.client.packets;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.net.ServerNetworking;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UpdatePartyC2SPacket {
    public static void send(KnownParty party) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(party.partyID());
        buf.writeString(party.name());
        ClientPlayNetworking.send(PacketIDs.UPDATE_PARTY_PACKET, buf);
    }
}
