package com.madmike.opatr.server.net;

import com.glisco.numismaticoverhaul.currency.CurrencyComponent;
import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.PacketIDs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.PartyManager;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;

import java.util.UUID;

public class BuyOfferReceiver {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIDs.BUY_OFFER_PACKET, (server, player, handler, buf, responseSender) -> {
            TradeOffer offer = TradeOffer.readFromBuf(buf);
            UUID buyerId = player.getUuid();
            IPartyManagerAPI pm = OpenPACServerAPI.get(server).getPartyManager();

            server.execute(() -> {
                OfferStorage storage = OfferStorage.get(server.getOverworld());

                if (!storage.getOffers().contains(offer)) {
                    player.sendMessage(Text.literal("§cOffer not found."), false);
                    return;
                }


                IServerPartyAPI buyerParty = pm.getPartyById(buyerId);

                UUID sellerId = offer.sellerID();
                UUID sellerParty = offer.partyID();

                double multiplier = 1.0;

                // Determine party affiliation logic
                boolean buyerInParty = buyerParty != null;
                boolean sellerInParty = sellerParty != null;

                if (buyerInParty && sellerInParty) {
                    if (!buyerParty.equals(sellerParty)) {
                        boolean areAllies = OpenPACServerAPI.get(server)
                                .getPartyManager()
                                .getPartiesThatAlly(buyerParty).toList().contains(sellerParty);
                                .getParty(buyerParty)
                                .isAlliedWith(sellerParty);
                        if (areAllies) {
                            multiplier = 0.5; // 50% discount for allies
                        }
                    }
                    // Else same party — no change
                } else if (buyerInParty ^ sellerInParty) {
                    multiplier = 2.0; // Society <-> Scallywag
                }

                long finalPrice = (long) Math.ceil(offer.price() * multiplier);

                // Get buyer currency component
                CurrencyComponent currency = CurrencyComponent.KEY.get(player);

                if (currency.getValue() < finalPrice) {
                    player.sendMessage(Text.literal("§cYou can't afford this item."), false);
                    return;
                }

                // Deduct coins and complete transaction
                currency.modify(-finalPrice);

                // Refund seller if online
                ServerPlayerEntity seller = server.getPlayerManager().getPlayer(sellerId);
                if (seller != null) {
                    CurrencyComponent.KEY.get(seller).modify(offer.price());
                } else {
                    // TODO: Offline seller handling (bank queue?)
                }

                // Give item to buyer
                if (!player.getInventory().insertStack(offer.item().copy())) {
                    player.sendMessage(Text.literal("§cNo room in your inventory."), false);
                    return;
                }

                // Remove offer from listing
                storage.removeOffer(offer);
                SyncRemoveOfferS2CPacket.sendToAll(offer);
                player.sendMessage(Text.literal("§aPurchase successful."), false);
            });
        });


    }
}
