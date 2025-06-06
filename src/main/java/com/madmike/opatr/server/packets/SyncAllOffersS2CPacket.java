package com.madmike.opatr.server.packets;

import com.madmike.opatr.server.net.ServerNetworking;
import com.madmike.opatr.server.trades.TradeOffer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class SyncAllOffersS2CPacket {

    public static void send(ServerPlayerEntity player, List<TradeOffer> offers) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(offers.size());

        for (TradeOffer offer : offers) {
            offer.writeToBuf(buf);
        }

        ServerPlayNetworking.send(player, PacketIDs.SYNC_ALL_OFFERS_PACKET, buf);
    }
}
