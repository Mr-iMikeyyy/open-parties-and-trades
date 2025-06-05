package com.madmike.opatr.client.gui;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;


public class TradingUIScreen extends BaseOwoScreen<FlowLayout> {

    public TradingUIScreen() {
        super(Text.literal("Trading Terminal"));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);

        FlowLayout tabBarContents = Containers.horizontalFlow(Sizing.content(), Sizing.content())
                .gap(4);

        for (TradeTab tab : tabs) {
            tabBarContents.child(
                    Components.button(Text.literal(tab.name()), b -> switchTab(tab))
            );
        }

        ScrollContainer<FlowLayout> tabBarScroll = Containers.horizontalScroll(Sizing.content(), Sizing.content(), tabBarContents);
        rootComponent.child(tabBarScroll);


        rootComponent.child(
                Containers.horizontalFlow(Sizing.content(), Sizing.content())
                        .child(Button.builder(Text.literal("My Offers"), button -> switchTab("my_offers")).build())
                        .child(Button.builder(Text.literal("Party Offers"), button -> switchTab("party_offers")).build())
                        .child(Button.builder(Text.literal("Sell"), button -> switchTab("sell")).build())
        );

        rootComponent.child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                .child(Label.of(Text.literal("Available Offers")))
                .child(Containers.grid(Sizing.fill(100), Sizing.fixed(100), 5)
                                .padding(Insets.of(5))
                                .child(new ItemComponent(new ItemStack(Items.DIAMOND)))
                        // dynamically add offers here
                )
        );
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }


    private void switchTab(TradeTab tab) {
        // Handle tab switching logic
        System.out.println("Switched to: " + tabId);
    }

    @Override
    public void tick() {
        super.tick();
        // Refresh offer data if needed
    }

    private Containers partySelection() {

    }
}
