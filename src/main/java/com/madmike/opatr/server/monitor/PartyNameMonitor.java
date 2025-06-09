package com.madmike.opatr.server.monitor;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.data.KnownPartyStorage;
import com.madmike.opatr.server.packets.party.SyncPartyS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.IPartyManager;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyNameMonitor {

    public static void register(MinecraftServer server) {
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            if (s.getTicks() % 60 != 0) return; // every 3 seconds

            IPartyManagerAPI partyManager = OpenPACServerAPI.get(server).getPartyManager();
            KnownPartyStorage storage = KnownPartyStorage.get(server.getOverworld());
            Map<UUID, KnownParty> knownParties = storage.getKnownParties();

            partyManager.getAllStream().forEach(party -> {
                UUID id = party.getId();
                String currentName = party.getDefaultName();
                KnownParty knownParty = knownParties.get(id);

                if (knownParty == null) {
                    // New party, add and sync
                    KnownParty newParty = new KnownParty(id, currentName);
                    storage.addOrUpdateParty(newParty);
                    SyncPartyS2CPacket.sendToAll(newParty, server);
                } else if (!currentName.equals(knownParty.name())) {
                    // Name has changed, update and sync
                    KnownParty updatedParty = new KnownParty(id, currentName);
                    storage.addOrUpdateParty(updatedParty);
                    SyncPartyS2CPacket.sendToAll(updatedParty, server);
                }
            });
        });
    }
}
