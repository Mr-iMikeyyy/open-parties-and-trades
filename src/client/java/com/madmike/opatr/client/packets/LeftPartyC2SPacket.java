package com.madmike.opatr.client.packets;

import com.madmike.opatr.server.net.ServerNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class LeftPartyC2SPacket {
    public static void send() {
        ClientPlayNetworking.send(ServerNetworking.LEFT_PARTY_PACKET, PacketByteBufs.empty());
    }
}
