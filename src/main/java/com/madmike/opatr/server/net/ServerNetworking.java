package com.madmike.opatr.server.net;

import com.glisco.numismaticoverhaul.ModComponents;
import com.madmike.opatr.server.packets.PacketIDs;
import com.madmike.opatr.server.packets.SyncAllOffersS2CPacket;
import com.madmike.opatr.server.trades.TradeOffer;
import com.madmike.opatr.server.data.OfferStorage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

public class ServerNetworking {

    public static void registerServerHandlers() {
        // Handle offer click (buy or cancel)
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.CLICK_OFFER_PACKET, (server, player, handler, buf, responseSender) -> {
            int tabOrdinal = buf.readInt();
            int index = buf.readInt();
            TradeTab tab = TradeTab.values()[tabOrdinal];

            server.execute(() -> {
                List<TradeOffer> offers = OfferStorage.getOffersFor(player.getUuid(), tab);
                if (index < 0 || index >= offers.size()) return;

                TradeOffer offer = offers.get(index);

                if (tab == TradeTab.MY_OFFERS && offer.seller().equals(player.getUuid())) {
                    OfferStorage.removeOffer(offer);
                    player.getInventory().insertStack(offer.item().copy());
                    player.sendMessage(Text.literal("Offer canceled."), false);
                } else {
                    var wallet = ModComponents.CURRENCY.get(player);
                    if (wallet.getValue() >= offer.price()) {
                        wallet.modify(-offer.price());
                        player.getInventory().insertStack(offer.item().copy());
                        OfferStorage.removeOffer(offer);
                        player.sendMessage(Text.literal("Purchase complete!"), false);
                    } else {
                        player.sendMessage(Text.literal("Not enough coins!"), false);
                    }
                }

                // Resend updated offers to reflect change
                List<TradeOffer> updated = TradeManager.getOffersFor(player.getUuid(), tab);
                SyncAllOffersS2CPacket.send(player, updated);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.SYNC_ALL_OFFERS_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // Perform cleanup or refresh on the server
                List<UUID> offers = OfferStorage.get(player.getServerWorld()).removePlayerOffers(player.getUuid());
            });
        });
    }
}
