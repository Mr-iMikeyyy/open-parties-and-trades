package com.madmike.opatr.client;

import com.madmike.opatr.client.monitor.PartyLeaveMonitor;
import com.madmike.opatr.client.net.ClientNetworking;
import com.madmike.opatr.server.OpenPartiesAndTrades;
import com.madmike.opatr.server.screens.ModScreens;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import com.madmike.opatr.client.gui.TradingScreen;
import net.minecraft.text.Text;

public class OpenPartiesAndTradesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        OpenPartiesAndTrades.LOGGER.info("Client initialized");

        HandledScreens.register(ModScreens.TRADING_SCREEN_HANDLER,
                (handler, inventory, title) -> new TradingScreen(handler, inventory, Text.literal("Trading Terminal"))
        );

        ClientNetworking.register();

        PartyLeaveMonitor.register();
    }
}