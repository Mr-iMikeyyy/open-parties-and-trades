package com.madmike.opatr.server.screens;

import com.madmike.opatr.server.OpenPartiesAndTrades;
import com.madmike.opatr.server.gui.TradingScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreens {
    public static ScreenHandlerType<TradingScreenHandler> TRADING_SCREEN_HANDLER;
    public static final Identifier TRADING_SCREEN_ID = new Identifier(OpenPartiesAndTrades.MOD_ID, "trading_screen");

    public static void registerScreenHandlers() {
        TRADING_SCREEN_HANDLER = new ScreenHandlerType<>(
                TradingScreenHandler::new, // must match (int, PlayerInventory)
                FeatureFlags.VANILLA_FEATURES
        );

        Registry.register(Registries.SCREEN_HANDLER, TRADING_SCREEN_ID, TRADING_SCREEN_HANDLER);
    }
}
