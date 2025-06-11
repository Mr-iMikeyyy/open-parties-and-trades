package com.madmike.opatr.client.gui;

import com.madmike.opatr.client.cache.PartyNameCache;
import com.madmike.opatr.server.data.TradeOffer;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.parties.party.api.IClientPartyAPI;

import java.util.*;

import static com.madmike.opatr.client.cache.OfferCache.OFFER_CACHE;


public class TradingScreen extends BaseOwoScreen<FlowLayout> {

    MinecraftClient client = MinecraftClient.getInstance();
    ClientPlayerEntity player = client.player;

    public TradingScreen() {
        super(Text.literal("Trading Terminal"));
    }

    List<TradeTab> tabs = new ArrayList<>();

    UUID myOffersTabID = UUID.randomUUID();
    UUID scallywagsTabID = UUID.randomUUID();
    UUID societyTabID = UUID.randomUUID();

    // Keep a reference to this container to swap tab contents later
    private FlowLayout offerListContainer;
    private FlowLayout detailsPanel;

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);

        // --- TAB BUTTON BAR ---
        FlowLayout tabBarContents = Containers.horizontalFlow(Sizing.content(), Sizing.content())
                .gap(4);

        List<TradeTab> tabs = buildTabs();

        if (tabs != null) {
            for (TradeTab tab : tabs) {
                tabBarContents.child(
                        Components.button(Text.literal(tab.name()), b -> switchTab(tab))
                );
            }
        } else {
            tabBarContents.child(Components.label(Text.literal(("No tabs to show"))));
        }


        ScrollContainer<FlowLayout> tabBarScroll = Containers.horizontalScroll(Sizing.content(), Sizing.content(), tabBarContents);
        rootComponent.child(tabBarScroll);

        // --- CONTENT AREA ---
        // --- CONTAINER SPLIT VIEW ---
        FlowLayout mainContent = Containers.horizontalFlow(Sizing.fill(100), Sizing.fill(100)).gap(10);

        // Left: Scrollable list of offers
        offerListContainer = Containers.verticalFlow(Sizing.content(), Sizing.fill(100)).gap(4);
        ScrollContainer<FlowLayout> scrollOffers = Containers.verticalScroll(Sizing.fill(60), Sizing.fill(100), offerListContainer);

        // Right: Disabled details + Buy button
        detailsPanel = Containers.verticalFlow(Sizing.fill(40), Sizing.fill(100)).gap(8);
        detailsPanel.surface(Surface.PANEL);
        detailsPanel.child(Components.button())

        // Add both to the main content row
        mainContent.child(scrollOffers);
        mainContent.child(detailsPanel);

        //Add Main Content to root
        rootComponent.child(mainContent);
    }

    private List<TradeTab> buildTabs() {
        MinecraftClient shopper = MinecraftClient.getInstance();
        ClientPlayerEntity player = shopper.player;
        if (player != null) {
            IClientPartyAPI shopperParty = OpenPACClientAPI.get().getClientPartyStorage().getParty();
            UUID shopperID = player.getUuid();

            if (shopperParty != null) {
                //Add my offers
                tabs.add(new TradeTab("My Offers", myOffersTabID));
                // Add my parties offers
                tabs.add(new TradeTab(shopperParty.getDefaultName(), shopperParty.getId(),
                        OFFER_CACHE.stream().filter(e -> e.partyID() == shopperParty.getId() && e.sellerID() != shopperID).toList()));
                //Add Ally parties
                shopperParty.getAllyPartiesStream().forEach(e ->
                        tabs.add(new TradeTab(PartyNameCache.PARTY_NAME_CACHE.get(e.getPartyId()), e.getPartyId(),
                                OFFER_CACHE.stream().filter(i -> i.partyID() == e.getPartyId()).toList())));
                tabs.add(new TradeTab("Scallywag", scallywagsTabID,
                        OFFER_CACHE.stream().filter(e -> e.partyID() == null).toList()));
            } else {
                tabs.add(new TradeTab("My Offers", myOffersTabID,
                        OFFER_CACHE.stream().filter(e -> e.sellerID() == shopper.player.getUuid()).toList()));
                tabs.add(new TradeTab("Scallywag", scallywagsTabID,
                        OFFER_CACHE.stream().filter(e -> e.partyID() == null && e.sellerID() != shopper.player.getUuid()).toList()));
                tabs.add(new TradeTab("Society", societyTabID,
                        OFFER_CACHE.stream().filter(e -> e.partyID() != null).toList()));
            }

            return tabs;
        } else return null;
    }


    private void switchTab(TradeTab tab) {
        tabContentContainer.clearChildren();
        if (player != null) {
            if (tab.partyId() == myOffersTabID) {
                List<TradeOffer> offers = OFFER_CACHE.stream().filter(e -> e.sellerID() == player.getUuid()).toList();
                for (TradeOffer offer : offers) {

                }
            } else {
                if (tab.name().equals("My Offers")) {
                    for (TradeOffer offer : tab.offers()) {
                        // Display each offer; this is a placeholder

                        tabContentContainer.child(
                                Components.label(Text.literal(offer.toString())) // Replace with actual rendering later
                        );
                    }
                }

            }
        }

    }
}
