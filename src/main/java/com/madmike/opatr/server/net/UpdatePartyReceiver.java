package com.madmike.opatr.server.net;

import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class UpdatePartyReceiver {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.UPDATE_PARTY_PACKET, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {

        }


    }
}
