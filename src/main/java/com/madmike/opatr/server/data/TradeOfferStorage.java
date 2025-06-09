package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradeOfferStorage extends PersistentState {
    public static final String SAVE_KEY = "opatr_offers";

    private final List<TradeOffer> offers = new ArrayList<>();

    public List<TradeOffer> getOffers() {
        return offers;
    }

    public void addOffer(TradeOffer offer) {
        offers.add(offer);
        markDirty(); // Important: mark the state dirty so it gets saved
    }

    public void removeOffer(TradeOffer offer) {
        offers.remove(offer);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList offerList = new NbtList();
        for (TradeOffer offer : offers) {
            offerList.add(offer.toNbt());
        }
        nbt.put("Offers", offerList);
        return nbt;
    }

    public static TradeOfferStorage createFromNbt(NbtCompound tag) {
        TradeOfferStorage state = new TradeOfferStorage();
        NbtList offerList = tag.getList("Offers", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : offerList) {
            if (element instanceof NbtCompound compound) {
                TradeOffer offer = TradeOffer.fromNbt(compound);
                state.offers.add(offer);
            }
        }
        return state;
    }

    public static TradeOfferStorage get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                TradeOfferStorage::createFromNbt,          // Deserializer
                TradeOfferStorage::new,                    // Constructor fallback
                SAVE_KEY                              // Unique key
        );
    }


    public List<UUID> removeAllOffersOfPlayer(UUID uuid) {
    }

    public void updatePlayerOffers(UUID playerID, UUID partyID) {
        for (int i = 0; i < offers.size(); i++) {
            TradeOffer offer = offers.get(i);
            if (offer.sellerID().equals(playerID)) {
                // Create a new offer with the updated party ID, keeping the same other fields
                TradeOffer updated = new TradeOffer(
                        offer.offerID(),
                        offer.sellerID(),
                        offer.item().copy(),
                        offer.price(),
                        partyID
                );
                offers.set(i, updated);
            }
        }
        markDirty();
    }
}
