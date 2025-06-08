package com.madmike.opatr.client.net;

import com.madmike.opatr.client.cache.PartyNameCache;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.*;

public class SyncAllPartiesReceiver {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_ALL_PARTIES_PACKET, (client, handler, buf, responseSender) -> {
            int size = buf.readInt();
            Map<UUID, String> receivedParties = new HashMap<>();

            for (int i = 0; i < size; i++) {
                UUID id = buf.readUuid();
                String name = buf.readString();
                receivedParties.put(id, name);
            }

            client.execute(() -> {
                PartyNameCache.KNOWN_PARTIES.clear();
                PartyNameCache.KNOWN_PARTIES.putAll(receivedParties);
            });
        });
    }
}
