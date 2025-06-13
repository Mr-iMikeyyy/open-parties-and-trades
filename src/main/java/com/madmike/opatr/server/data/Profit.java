package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public record Profit (UUID sellerID, long profitAmount) {
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("SellerID", sellerID);
        nbt.putLong("Profit", profitAmount);
        return nbt;
    }

    public static Profit fromNbt(NbtCompound nbt) {
        UUID partyID = nbt.getUuid("SellerID");
        long profit = nbt.getLong("Profit");
        return new Profit(partyID, profit);
    }
}
