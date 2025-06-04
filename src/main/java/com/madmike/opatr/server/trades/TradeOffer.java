package com.madmike.opatr.server.trades;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public record TradeOffer(UUID seller, ItemStack item, long price, UUID sellerParty) {

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("Seller", this.seller);
        nbt.put("Item", this.item.writeNbt(new NbtCompound()));
        nbt.putLong("Price", this.price);
        nbt.putUuid("Party", this.sellerParty);
        return nbt;
    }

    public static TradeOffer fromNbt(NbtCompound nbt) {
        UUID seller = nbt.getUuid("Seller");
        ItemStack item = ItemStack.fromNbt(nbt.getCompound("Item"));
        long price = nbt.getLong("Price");
        UUID party = nbt.getUuid("Party");
        return new TradeOffer(seller, item, price, party);
    }

    public void writeToBuf(PacketByteBuf buf) {
        buf.writeUuid(seller);
        buf.writeItemStack(item);
        buf.writeLong(price);
        buf.writeBoolean(sellerParty != null);
        if (sellerParty != null) buf.writeUuid(sellerParty);
    }

    public static TradeOffer readFromBuf(PacketByteBuf buf) {
        UUID seller = buf.readUuid();
        ItemStack item = buf.readItemStack();
        long price = buf.readLong();
        UUID party = buf.readBoolean() ? buf.readUuid() : null;
        return new TradeOffer(seller, item, price, party);
    }
}
