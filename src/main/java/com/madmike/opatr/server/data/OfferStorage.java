package com.madmike.opatr.server.data;

import com.madmike.opatr.server.trades.TradeOffer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class OfferStorage extends PersistentState {
    public static final String SAVE_KEY = "open_parties_and_trading_offers";

    private final List<TradeOffer> offers = new ArrayList<>();

    public List<TradeOffer> getOffers() {
        return offers;
    }

    public List<UUID> removePlayerOffers(UUID playerId) {
        List<TradeOffer> removedOffers = new ArrayList<>();
        List<UUID> removedIds = new ArrayList<>();

        Iterator<TradeOffer> iterator = this.offers.iterator();
        while (iterator.hasNext()) {
            TradeOffer offer = iterator.next();
            if (offer.seller().equals(playerId)) {
                removedOffers.add(offer);
                removedIds.add(offer.offerId());
                iterator.remove(); // Remove from the list safely during iteration
            }
        }

        markDirty();

        // Give items back to the player if they’re online
        ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(playerUuid);
        if (player != null) {
            for (TradeOffer offer : removedOffers) {
                player.giveItemStack(offer.item());
            }
        }

        return removedIds;
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

    public static OfferStorage createFromNbt(NbtCompound tag) {
        OfferStorage state = new OfferStorage();
        NbtList offerList = tag.getList("Offers", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : offerList) {
            if (element instanceof NbtCompound compound) {
                TradeOffer offer = TradeOffer.fromNbt(compound);
                state.offers.add(offer);
            }
        }
        return state;
    }

    public static OfferStorage get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                OfferStorage::createFromNbt,          // Deserializer
                OfferStorage::new,                    // Constructor fallback
                SAVE_KEY                              // Unique key
        );
    }
}
