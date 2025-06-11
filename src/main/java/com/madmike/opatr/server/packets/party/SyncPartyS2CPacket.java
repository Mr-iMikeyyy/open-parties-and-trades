package com.madmike.opatr.server.packets.party;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class SyncPartyS2CPacket {
    public static void sendToAll(KnownParty updatedParty, MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        updatedParty.writeToBuf(buf);

        CompletableFuture.runAsync(() -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, PacketIDs.SYNC_PARTY_PACKET, buf);
            }
        });
    }
}
