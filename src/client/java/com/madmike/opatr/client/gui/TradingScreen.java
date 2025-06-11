package com.madmike.opatr.client.gui;

import com.madmike.opatr.client.cache.PartyNameCache;
import com.madmike.opatr.client.packets.offers.BuyOfferC2SPacket;
import com.madmike.opatr.server.data.TradeOffer;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
                // Add party tab
                tabs.add(new TradeTab(shopperParty.getDefaultName(), shopperParty.getId()));
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
                //MY offers
                List<TradeOffer> offers = OFFER_CACHE.stream().filter(e -> e.sellerID() == player.getUuid()).toList();
                // Party Offers
                OFFER_CACHE.stream().filter(e -> e.partyID() == shopperParty.getId() && e.sellerID() != shopperID).toList())
                for (TradeOffer offer : offers) {

                }
            } else {
                if (tab.name().equals("My Offers")) {
                    for (TradeOffer offer : tab.offers()) {
                        // Display each offer; this is a placeholder

                        offerListContainer.child(createOfferEntry(offer));
                    }
                }

            }
        }
    }

    private Component createOfferEntry(TradeOffer offer) {
        ItemStack stack = offer.item();
        Text priceText = Text.literal(String.valueOf(offer.price())).formatted(Formatting.GOLD); // format however you like

        // --- Icon for item ---
        ItemComponent itemIcon = Components.item(stack);

        // --- Price label ---
        LabelComponent priceLabel = Components.label(priceText)
                .horizontalTextAlignment(HorizontalAlignment.RIGHT); // optional gold color

        // --- Row container for this offer ---
        FlowLayout row = Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(20))
                .gap(6)
                .cursorStyle(CursorStyle.HAND)

                .surface(Surface.DARK_PANEL)
                ;
//                .mouseDown().subscribe((mouseX, mouseY, button) -> {
//                    selectOffer(offer);
//                    return true;
//                });

        // Stretch label to fill available space between icon and price
        LabelComponent itemLabel = Components.label(stack.getName())
                .sizing(Sizing.fill(100));

        row.child(itemIcon);
        row.child(itemLabel);
        row.child(priceLabel);

        return row;
    }

    private void selectOffer(TradeOffer offer) {
        detailsPanel.clearChildren();

        detailsPanel.child(Components.label(Text.literal("Item: " + offer.item().getName().getString())));
        detailsPanel.child(Components.label(Text.literal("Price: " + offer.priceString())));

        ButtonComponent buyButton = Components.button(Text.literal("Buy"), b -> {
            // TODO: Send purchase request to server
            BuyOfferC2SPacket.send();
        });

        detailsPanel.child(buyButton);
    }
}
