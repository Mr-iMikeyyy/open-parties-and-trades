package com.madmike.opatr.client.packets;

import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class BuyOfferC2SPacket {
    public static void send(TradeOffer offer) {
        PacketByteBuf buf = PacketByteBufs.create();
        offer.writeToBuf(buf);
        ClientPlayNetworking.send(PacketIDs.BUY_OFFER_PACKET, buf);
    }
}
