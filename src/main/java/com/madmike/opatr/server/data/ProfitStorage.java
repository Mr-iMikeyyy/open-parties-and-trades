package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfitStorage extends PersistentState {
    public static final String SAVE_KEY = "opatr_profits";

    private final List<Profit> profits = new ArrayList<>();

    public boolean hasProfitFor(UUID playerId) {
        return profits.stream().anyMatch(p -> p.sellerID().equals(playerId));
    }

    public List<Profit> getProfitsFor(UUID playerId) {
        return profits.stream().filter(p -> p.sellerID().equals(playerId)).toList();
    }

    public void clearProfitsFor(UUID playerId) {
        profits.removeIf(p -> p.sellerID().equals(playerId));
        markDirty();
    }

    public void addProfit(Profit profit) {
        profits.add(profit);
        markDirty(); // Important: mark the state dirty so it gets saved
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList profitList = new NbtList();
        for (Profit profit : profits) {
            profitList.add(profit.toNbt());
        }
        nbt.put("Profits", profitList);
        return nbt;
    }

    public static ProfitStorage createFromNbt(NbtCompound tag) {
        ProfitStorage state = new ProfitStorage();
        NbtList profitList = tag.getList("Profits", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : profitList) {
            if (element instanceof NbtCompound compound) {
                Profit profit = Profit.fromNbt(compound);
                state.profits.add(profit);
            }
        }
        return state;
    }

    public static ProfitStorage get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                ProfitStorage::createFromNbt,          // Deserializer
                ProfitStorage::new,                    // Constructor fallback
                SAVE_KEY                              // Unique key
        );
    }
}
