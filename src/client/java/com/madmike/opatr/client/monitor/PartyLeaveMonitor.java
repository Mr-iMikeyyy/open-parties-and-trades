package com.madmike.opatr.client.monitor;

import com.madmike.opatr.client.packets.PartyChangeC2SPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.parties.party.api.IClientPartyAPI;

import java.util.UUID;

public class PartyLeaveMonitor {
    private static IClientPartyAPI lastKnownParty = OpenPACClientAPI.get().getClientPartyStorage().getParty();
    private static int tickCounter = 0;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            tickCounter++;
            if (tickCounter >= 20) { // every 20 ticks (1 second)
                tickCounter = 0;
                checkPartyStatus();
            }
        });
    }

    private static void checkPartyStatus() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        UUID playerId = client.player.getUuid();
        IClientPartyAPI currentParty = OpenPACClientAPI.get().getClientPartyStorage().getParty();

        // Detect leave
        if (lastKnownParty != currentParty) {
            UUID partyId = currentParty != null ? currentParty.getId() : null;
            PartyChangeC2SPacket.send(playerId, partyId); // Safely send null if not in a party
        }

        lastKnownParty = currentParty;
    }
}
