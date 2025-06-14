package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellerStorage extends PersistentState {

    public static final String SAVE_KEY = "opatr_sellers";

    private final Map<UUID, Seller> sellers = new HashMap<>();

    public Seller getSeller(UUID playerId) {
        return sellers.computeIfAbsent(playerId, id -> new Seller(id, 0, 1));
    }

    public void updateSeller(UUID playerId, long salesDelta, int slotDelta) {
        Seller record = getSeller(playerId);
        record.sales += salesDelta;
        record.slots += slotDelta;
        markDirty();
    }

    public void setSales(UUID playerId, long sales) {
        getSeller(playerId).sales = sales;
        markDirty();
    }

    public void setSlots(UUID playerId, int slots) {
        getSeller(playerId).slots = slots;
        markDirty();
    }

    public static SellerStorage get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                SellerStorage::createFromNbt,
                SellerStorage::new,
                SAVE_KEY
        );
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList sellerList = new NbtList();
        for (Seller record : sellers.values()) {
            sellerList.add(record.toNbt());
        }
        nbt.put("Sellers", sellerList);
        return nbt;
    }

    public static SellerStorage createFromNbt(NbtCompound nbt) {
        SellerStorage storage = new SellerStorage();
        NbtList sellerList = nbt.getList("Sellers", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : sellerList) {
            if (element instanceof NbtCompound compound) {
                SellerRecord record = SellerRecord.fromNbt(compound);
                storage.sellers.put(record.playerId(), record);
            }
        }
        return storage;
    }
}
