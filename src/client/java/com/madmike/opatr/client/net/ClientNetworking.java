package com.madmike.opatr.client.net;

import com.madmike.opatr.client.net.offers.SyncAddOfferReceiver;
import com.madmike.opatr.client.net.offers.SyncAllOffersReceiver;
import com.madmike.opatr.client.net.offers.SyncRemoveOfferReceiver;
import com.madmike.opatr.client.net.party.SyncAllPartiesReceiver;
import com.madmike.opatr.client.net.party.SyncPartyReceiver;
import com.madmike.opatr.client.net.party.SyncPlayerPartyChangeReceiver;

public class ClientNetworking {
    public static void register() {

        SyncAddOfferReceiver.register();
        SyncAllOffersReceiver.register();
        SyncAllPartiesReceiver.register();
        SyncPlayerPartyChangeReceiver.register();
        SyncRemoveOfferReceiver.register();
        SyncPartyReceiver.register();

    }
}
