package com.madmike.opatr.server.net;

import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class BuyOfferReceiver {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.BUY_OFFER_PACKET, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {

        }


    }
}
