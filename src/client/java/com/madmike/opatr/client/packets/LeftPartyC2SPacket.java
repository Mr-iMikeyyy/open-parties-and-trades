package com.madmike.opatr.client.packets;

import com.madmike.opatr.server.OpenPartiesAndTrades;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.Identifier;

public class LeftPartyC2SPacket {
    public static final Identifier ID = new Identifier(OpenPartiesAndTrades.MOD_ID, "left_party");

    public static void send() {
        ClientPlayNetworking.send(ID, PacketByteBufs.empty());
    }
}
