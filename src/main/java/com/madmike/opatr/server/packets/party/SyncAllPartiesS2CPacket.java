package com.madmike.opatr.server.packets.party;

import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncAllPartiesS2CPacket {
    public static void send(ServerPlayerEntity player, HashMap<UUID, String> parties) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(parties.size());

        for (Map.Entry<UUID, String> entry : parties.entrySet()) {
            buf.writeUuid(entry.getKey());
            buf.writeString(entry.getValue());
        }

        ServerPlayNetworking.send(player, PacketIDs.SYNC_ALL_PARTIES_PACKET, buf);
    }
}
