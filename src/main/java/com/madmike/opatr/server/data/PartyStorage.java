package com.madmike.opatr.server.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.*;

public class PartyStorage extends PersistentState {
    public static final String SAVE_KEY = "opatr_parties";

    private final Map<UUID, KnownParty> knownParties = new HashMap<>();

    public Map<UUID, KnownParty> getKnownParties() {
        return knownParties;
    }

    public void addNewParty(KnownParty party) {
        knownParties.put(party.partyID(), party);
        markDirty(); // Important: mark the state dirty so it gets saved
    }

    public void updatePartyName(KnownParty updatedParty) {
        knownParties.put(updatedParty.partyID(), updatedParty);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList knownPartyList = new NbtList();
        for (KnownParty party : knownParties.values()) {
            knownPartyList.add(party.toNbt());
        }
        nbt.put("KnownParties", knownPartyList);
        return nbt;
    }

    public static PartyStorage createFromNbt(NbtCompound tag) {
        PartyStorage state = new PartyStorage();
        NbtList knownPartyList = tag.getList("KnownParties", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : knownPartyList) {
            KnownParty party = KnownParty.fromNbt((NbtCompound) element);
            state.knownParties.put(party.partyID(), party);
        }
        return state;
    }

    public static PartyStorage get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                PartyStorage::createFromNbt,          // Deserializer
                PartyStorage::new,                    // Constructor fallback
                SAVE_KEY                              // Unique key
        );
    }
}
