package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public record KnownParty(UUID partyID, String name) {
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("PartyID", partyID);
        nbt.putString("Name", name);
        return nbt;
    }

    public static KnownParty fromNbt(NbtCompound nbt) {
        UUID partyID = nbt.getUuid("PartyID");
        String name = nbt.getString("Name");
        return new KnownParty(partyID, name);
    }

    public void writeToBuf(PacketByteBuf buf) {
        buf.writeUuid(partyID);
        buf.writeString(name);
    }

    public static KnownParty readFromBuf(PacketByteBuf buf) {
        UUID partyID = buf.readUuid();
        String name = buf.readString();
        return new KnownParty(partyID, name);
    }
}
