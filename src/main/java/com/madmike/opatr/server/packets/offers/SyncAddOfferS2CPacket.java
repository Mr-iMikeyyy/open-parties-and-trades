package com.madmike.opatr.server.packets.offers;

import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.concurrent.CompletableFuture;

public class SyncAddOfferS2CPacket {

    public static void sendToAll(TradeOffer offer, MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        offer.writeToBuf(buf);

        // Get all online players and send the packet to each
        server.execute(() -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, PacketIDs.SYNC_ADD_OFFER_PACKET, buf);
            }
        });
    }
}
