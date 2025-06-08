package com.madmike.opatr.server.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record TradeOffer(UUID offerID, UUID sellerID, ItemStack item, long price, @Nullable UUID partyID) {

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("OfferID", this.offerID);
        nbt.putUuid("Seller", this.sellerID);
        nbt.put("Item", this.item.writeNbt(new NbtCompound()));
        nbt.putLong("Price", this.price);

        if (this.partyID != null) {
            nbt.putUuid("PartyID", this.partyID);
            nbt.putBoolean("HasParty", true);
        } else {
            nbt.putBoolean("HasParty", false);
        }

        return nbt;
    }

    public static TradeOffer fromNbt(NbtCompound nbt) {
        UUID offerId = nbt.getUuid("OfferId");
        UUID seller = nbt.getUuid("Seller");
        ItemStack item = ItemStack.fromNbt(nbt.getCompound("Item"));
        long price = nbt.getLong("Price");

        UUID party = nbt.getBoolean("HasParty") ? nbt.getUuid("Party") : null;

        return new TradeOffer(offerId, seller, item, price, party);
    }

    public void writeToBuf(PacketByteBuf buf) {
        buf.writeUuid(offerID);
        buf.writeUuid(sellerID);
        buf.writeItemStack(item);
        buf.writeLong(price);
        buf.writeBoolean(partyID != null);
        if (partyID != null) buf.writeUuid(partyID);
    }

    public static TradeOffer readFromBuf(PacketByteBuf buf) {
        UUID offerId = buf.readUuid();
        UUID seller = buf.readUuid();
        ItemStack item = buf.readItemStack();
        long price = buf.readLong();
        UUID party = buf.readBoolean() ? buf.readUuid() : null;
        return new TradeOffer(offerId, seller, item, price, party);
    }
}
