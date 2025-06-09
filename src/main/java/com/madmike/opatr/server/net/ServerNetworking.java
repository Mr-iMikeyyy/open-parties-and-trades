package com.madmike.opatr.server.net;

import com.madmike.opatr.server.net.offers.BuyOfferReceiver;
import com.madmike.opatr.server.net.offers.RemoveOfferReceiver;
import com.madmike.opatr.server.net.party.PartyChangeReceiver;

public class ServerNetworking {

    public static void registerServerHandlers() {
        BuyOfferReceiver.register();
        PartyChangeReceiver.register();
        RemoveOfferReceiver.register();
    }
}
