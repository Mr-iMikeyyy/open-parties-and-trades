package com.madmike.opatr.client.gui;

import com.glisco.numismaticoverhaul.item.NumismaticOverhaulItems;
import com.madmike.opatr.server.OpenPartiesAndTrades;
import com.madmike.opatr.server.gui.TradingScreenHandler;
import com.madmike.opatr.server.trades.TradeOffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.common.parties.party.Party;
import xaero.pac.common.parties.party.member.api.IPartyMemberAPI;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.madmike.opatr.client.cache.OfferCache.CLIENT_OFFERS;


public class TradingScreen extends HandledScreen<TradingScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/generic_54.png");

    private TradeTab currentTab = TradeTab.MY_OFFERS;

    private final List<TradeOffer> offers = new ArrayList<>();

    public TradingScreen(TradingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - textRenderer.getWidth(title)) / 2;

        int tabX = this.x + 8;
        int tabY = this.y - 18;

        for (TradeTab tab : TradeTab.values()) {
            ButtonWidget button = ButtonWidget.builder(
                    Text.literal(tab.getLabel()),
                    b -> switchTab(tab)
            ).dimensions(tabX, tabY, 90, 16).build();

            this.addDrawableChild(button);
            tabX += 92;
        }

        if (currentTab == TradeTab.SELL) {
            setupSellTab();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawBackground(context, delta, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        drawWallet(context);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(
                TEXTURE,
                this.x, this.y,         // Destination on screen
                0, 0,                   // Source texture UV
                this.backgroundWidth, this.backgroundHeight
        );
    }

    private void drawWallet(DrawContext context) {
        // 💰 Draw the coin balance from the screen handler
        long balance = this.handler.getCoinBalance();
        String balanceText = "Wallet: " + balance + " coins";
        // Choose a spot to draw it — top left corner of the GUI, adjust as needed
        int textX = this.x + 10;
        int textY = this.y + 6;
        context.drawText(this.textRenderer, balanceText, textX, textY, 0xFFFFFF, false);
        ItemStack coin = new ItemStack(NumismaticOverhaulItems.GOLD_COIN); // or your actual coin item
        context.drawItem(coin, this.x + 5, this.y + 20);
    }

    private void switchTab(TradeTab tab) {
        this.currentTab = tab;
        OpenPartiesAndTrades.LOGGER.info("Switched to tab: " + tab.name());

        this.offers.clear();

        UUID self = this.client.player.getUuid();

        switch (currentTab) {
            case MY_OFFERS -> {
                CLIENT_OFFERS.stream()
                        .filter(offer -> offer.seller().equals(self))
                        .forEach(this.offers::add);
            }

            case PARTY_OFFERS -> {
                var party = OpenPACClientAPI.get()
                        .getClientPartyStorage()
                        .getParty();

                if (party != null) {
                    Set<UUID> partyMembers = party.getMemberInfoStream()
                            .map(IPartyMemberAPI::getUUID)
                            .collect(Collectors.toSet());

                    CLIENT_OFFERS.stream()
                            .filter(offer -> partyMembers.contains(offer.seller()))
                            .forEach(this.offers::add);
                }
            }

            case ALLY_OFFERS -> {
                Optional<Party> myParty = OpenPartiesAndClaims.API.getPartyForPlayer(self);
                if (myParty.isPresent()) {
                    Set<UUID> allies = myParty.get().getAllies(); // Assuming such a method exists
                    CLIENT_OFFERS.stream()
                            .filter(offer -> allies.contains(offer.seller()))
                            .forEach(this.offers::add);
                }
            }

            case SCALLYWAG_OFFERS -> {
                Optional<Party> myParty = OpenPartiesAndClaims.API.getPartyForPlayer(self);
                Set<UUID> excluded = new HashSet<>();
                excluded.add(self);

                myParty.ifPresent(party -> {
                    party.getMembers().stream().map(Member::uuid).forEach(excluded::add);
                    party.getAllies().forEach(excluded::add);
                });

                CLIENT_OFFERS.stream()
                        .filter(offer -> !excluded.contains(offer.seller()))
                        .forEach(this.offers::add);
            }

            default -> {
            }

            // Clear and reinitialize tab-specific widgets
//        this.clearChildren();
//        this.init();
        }
    }

    private void setupSellTab() {
        // Add custom inventory with 1 slot
        SimpleInventory sellInventory = new SimpleInventory(1);
        ItemStack stack = this.handler.getSellSlotStack();

        // Coin input fields
        goldField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + 60, 40, 18, Text.literal("Gold"));
        silverField = new TextFieldWidget(this.textRenderer, this.x + 60, this.y + 60, 40, 18, Text.literal("Silver"));
        bronzeField = new TextFieldWidget(this.textRenderer, this.x + 110, this.y + 60, 40, 18, Text.literal("Bronze"));

        goldField.setText("0");
        silverField.setText("0");
        bronzeField.setText("0");

        // Only allow digits
        Predicate<String> isNumeric = s -> s.matches("\\d*");
        goldField.setTextPredicate(isNumeric);
        silverField.setTextPredicate(isNumeric);
        bronzeField.setTextPredicate(isNumeric);

        this.addDrawableChild(goldField);
        this.addDrawableChild(silverField);
        this.addDrawableChild(bronzeField);

        // List button
        listButton = ButtonWidget.builder(Text.literal("List Item"), btn -> {
            try {
                int gold = Integer.parseInt(goldField.getText());
                int silver = Integer.parseInt(silverField.getText());
                int bronze = Integer.parseInt(bronzeField.getText());
                long price = CoinValueConverter.toBronze(gold, silver, bronze);

                ItemStack stack = sellInventory.getStack(0);
                if (!stack.isEmpty() && price > 0) {
                    ListOfferC2SPacket.send(stack, price);
                    stack.setCount(0);
                }
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        }).dimensions(this.x + 10, this.y + 90, 80, 20).build();

        this.addDrawableChild(listButton);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int itemY = this.y + 28;

        for (int i = 0; i < offers.size(); i++) {
            int itemX = this.x + 10 + i * 20;
            if (mouseX >= itemX && mouseX < itemX + 16 && mouseY >= itemY && mouseY < itemY + 16) {

                // Send a packet to the server with index of clicked offer
                ClickOfferC2SPacket.send(currentTab, i);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}

