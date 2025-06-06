package com.madmike.opatr.client.gui;

import com.madmike.opatr.client.cache.OfferCache;
import com.madmike.opatr.client.cache.PartyNameCache;
import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.trades.TradeOffer;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.parties.party.api.IClientPartyAPI;

import java.util.*;


public class TradingScreen extends BaseOwoScreen<FlowLayout> {

    public TradingScreen() {
        super(Text.literal("Trading Terminal"));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);

        FlowLayout tabBarContents = Containers.horizontalFlow(Sizing.content(), Sizing.content())
                .gap(4);

        for (TradeTab tab : buildTabs()) {
            tabBarContents.child(
                    Components.button(Text.literal(tab.name()), b -> switchTab(tab))
            );
        }

        ScrollContainer<FlowLayout> tabBarScroll = Containers.horizontalScroll(Sizing.content(), Sizing.content(), tabBarContents);
        rootComponent.child(tabBarScroll);

    }

    private List<TradeTab> buildTabs() {
        Map<UUID, String> partyNames = getUuidStringMap();

        List<TradeTab> tabs = new ArrayList<>();

        // Add your own party tab first (you'll need to replace with actual logic)
        UUID myPartyId = getMyPartyId(); // implement this based on OpenPACClientAPI
        if (myPartyId != null) {
            tabs.add(new TradeTab("Your Party", myPartyId));
        } else {
            tabs.add(new TradeTab("Scallywags", null));
        }

        // Add the other parties, sorted by name
        partyNames.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(myPartyId))
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> new TradeTab(entry.getValue(), entry.getKey()))
                .forEach(tabs::add);

        return tabs;
    }

    private UUID getMyPartyId() {
        IClientPartyAPI party = OpenPACClientAPI.get().getClientPartyStorage().getParty();
        if (party != null) {
            return party.getId();
        }
        else {
            return null;
        }
    }

    private static @NotNull Map<UUID, String> getUuidStringMap() {
        Map<UUID, String> partyNames = new HashMap<>();
        Set<UUID> seenPartyIds = new HashSet<>();

        // Collect party IDs and their names from offers
        for (TradeOffer offer : OfferCache.CLIENT_OFFERS) {
            UUID partyId = offer.sellerParty();
            if (partyId != null && seenPartyIds.add(partyId)) {
                String name = PartyNameCache.getPartyNameByID(partyId);
                if (name != null) {
                    partyNames.put(partyId, name);
                }
                else {
                    partyNames.put(partyId, partyId.toString().substring(0, 8));
                }
            }
        }
        return partyNames;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }


    private void switchTab(TradeTab tab) {
        // Handle tab switching logic
        System.out.println("Switched to: " + tab.partyId());
    }
}
