package com.madmike.opatr.client.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyNameCache {
    private static Map<UUID, String> PARTY_ID_NAME_MAP = new HashMap<>();
    public static String getPartyNameByID(UUID partyId) {
        return PARTY_ID_NAME_MAP.get(partyId);
    }
}
