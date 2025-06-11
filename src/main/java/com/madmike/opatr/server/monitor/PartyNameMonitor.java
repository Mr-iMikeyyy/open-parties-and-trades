package com.madmike.opatr.server.monitor;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.data.KnownPartyStorage;
import com.madmike.opatr.server.packets.party.SyncPartyS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PartyNameMonitor {

    public static void register(MinecraftServer server) {
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            if (s.getTicks() % 60 != 0) return; // every 3 seconds

            IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
            KnownPartyStorage partyStorage = KnownPartyStorage.get(server.getOverworld());

            partyManager.getAllStream().forEach(party -> {
                UUID id = party.getId();
                String currentName = party.getDefaultName();

                KnownParty known = partyStorage.getKnownParties().get(id);

                // If unknown party, add and send
                if (known == null) {
                    KnownParty newParty = new KnownParty(id, currentName);
                    partyStorage.addOrUpdateParty(newParty);
                    sendUpdateAsync(server, newParty);
                    return;
                }

                // If name changed, update and send
                if (!currentName.equals(known.name())) {
                    KnownParty updatedParty = new KnownParty(id, currentName);
                    partyStorage.addOrUpdateParty(updatedParty);
                    sendUpdateAsync(server, updatedParty);
                }
            });
        });
    }

    private static void sendUpdateAsync(MinecraftServer server, KnownParty party) {
        CompletableFuture.runAsync(() -> {
            SyncPartyS2CPacket.sendToAll(party, server);
        });
    }
}
