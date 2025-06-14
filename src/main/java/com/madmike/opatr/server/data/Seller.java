package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public record Seller(UUID playerId, long sales, int slots) {

    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putUuid("PlayerId", playerId);
        tag.putLong("Sales", sales);
        tag.putInt("Slots", slots);
        return tag;
    }

    public static Seller fromNbt(NbtCompound tag) {
        UUID playerId = tag.getUuid("PlayerId");
        long sales = tag.getLong("Sales");
        int slots = tag.getInt("Slots");
        return new Seller(playerId, sales, slots);
    }
}
