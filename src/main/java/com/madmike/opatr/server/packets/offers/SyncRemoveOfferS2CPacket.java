package com.madmike.opatr.server.packets.offers;

import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class SyncRemoveOfferS2CPacket {
    public static void sendToAll(TradeOffer offer, MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(offer.offerID());
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, PacketIDs.SYNC_REMOVE_OFFER_PACKET, buf);
        }
    }
}
