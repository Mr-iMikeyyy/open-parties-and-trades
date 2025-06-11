package com.madmike.opatr.server.packets.party;

import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SyncPlayerPartyChangeS2CPacket {
    public static void sendToAll(UUID playerID, @Nullable UUID partyId, MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(playerID);

        if (partyId != null) {
            buf.writeBoolean(true);
            buf.writeUuid(partyId);
        }
        else {
            buf.writeBoolean(false);
        }
        CompletableFuture.runAsync(() -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, PacketIDs.SYNC_PLAYER_PARTY_CHANGE_PACKET, buf);
            }
        });
    }
}
