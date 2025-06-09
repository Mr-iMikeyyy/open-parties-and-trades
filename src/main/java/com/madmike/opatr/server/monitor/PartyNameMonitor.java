package com.madmike.opatr.server.monitor;

import com.madmike.opatr.server.data.KnownPartyStorage;
import com.madmike.opatr.server.packets.party.SyncPartyS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyNameMonitor {

    public static void register(MinecraftServer server) {
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            if (s.getTicks() % 60 != 0) return; // every 3 second

            var partyManager = OpenPACServerAPI.get(server).getPartyManager();

            partyManager.getAllStream().forEach(party -> {
                UUID id = party.getId();
                String currentName = party.getDefaultName();

                String knownName = KnownPartyStorage.get(server.getOverworld()).getKnownParties().get(id).name();

                if (knownName == null) {
                    lastKnownNames.put(id, currentName);
                    return;
                }

                if (!currentName.equals(knownName)) {
                    lastKnownNames.put(id, currentName);
                    SyncPartyS2CPacket.sendToAll(id, currentName);
                }
            });
        });
    }
}
