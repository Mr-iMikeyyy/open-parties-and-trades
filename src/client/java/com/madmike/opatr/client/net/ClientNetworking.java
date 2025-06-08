package com.madmike.opatr.client.net;

public class ClientNetworking {
    public static void register() {

        SyncAddOfferReceiver.register();
        SyncAllOffersReceiver.register();
        SyncAllPartiesReceiver.register();
        SyncNewPartyReceiver.register();
        SyncPartyChangeReceiver.register();
        SyncRemoveOfferReceiver.register();
        SyncUpdatePartyReceiver.register();

    }
}
