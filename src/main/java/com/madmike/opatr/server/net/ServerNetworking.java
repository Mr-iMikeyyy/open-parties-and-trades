package com.madmike.opatr.server.net;

public class ServerNetworking {

    public static void registerServerHandlers() {
        BuyOfferReceiver.register();
        PartyChangeReceiver.register();
        RemoveOfferReceiver.register();
        UpdatePartyReceiver.register();
    }
}
