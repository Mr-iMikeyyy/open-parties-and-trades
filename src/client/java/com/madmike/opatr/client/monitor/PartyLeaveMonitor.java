package com.madmike.opatr.client.monitor;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.parties.party.api.IClientPartyAPI;

public class PartyLeaveMonitor {
    private static IClientPartyAPI lastKnownParty = null;
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
        IClientPartyAPI currentParty = OpenPACClientAPI.get().getClientPartyStorage().getParty();

        // Detect leave
        if (lastKnownParty != null && currentParty == null) {
            LeftPartyC2SPacket.send();
            lastKnownParty = null;
        }
    }
}
