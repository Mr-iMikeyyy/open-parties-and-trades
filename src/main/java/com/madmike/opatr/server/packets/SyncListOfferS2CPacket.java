package com.madmike.opatr.server.packets;

import com.madmike.opatr.server.trades.TradeOffer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncListOfferS2CPacket {

    public static void sendToAll(TradeOffer offer, MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        offer.writeToBuf(buf);

        // Get all online players and send the packet to each
        if (server != null) {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, PacketIDs.SYNC_ADD_OFFER_PACKET, buf);
            }
        }
    }
}
