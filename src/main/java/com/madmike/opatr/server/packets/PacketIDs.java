package com.madmike.opatr.server.packets;

import com.madmike.opatr.server.OPATR;
import net.minecraft.util.Identifier;

public class PacketIDs {
    //Client Packets
    public static final Identifier BUY_OFFER_PACKET = new Identifier(OPATR.MOD_ID, "buy_offer");
    public static final Identifier PARTY_CHANGE_PACKET = new Identifier(OPATR.MOD_ID, "party_change");
    public static final Identifier REMOVE_OFFER_PACKET = new Identifier(OPATR.MOD_ID, "remove_offer");

    //Server Packets
    public static final Identifier SYNC_ADD_OFFER_PACKET = new Identifier(OPATR.MOD_ID, "sync_add_offer");
    public static final Identifier SYNC_ALL_OFFERS_PACKET = new Identifier(OPATR.MOD_ID, "sync_all_offers");
    public static final Identifier SYNC_REMOVE_OFFER_PACKET = new Identifier(OPATR.MOD_ID, "sync_remove_offer");

    public static final Identifier SYNC_ALL_PARTIES_PACKET = new Identifier(OPATR.MOD_ID, "sync_all_parties");
    public static final Identifier SYNC_PARTY_PACKET = new Identifier(OPATR.MOD_ID, "sync_party");
    public static final Identifier SYNC_PLAYER_PARTY_CHANGE_PACKET = new Identifier(OPATR.MOD_ID, "sync_player_party_change");
}
