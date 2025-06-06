package com.madmike.opatr.client;

import com.madmike.opatr.client.gui.TradingScreen;
import com.madmike.opatr.client.monitor.PartyLeaveMonitor;
import com.madmike.opatr.client.net.SyncOffersReceiver;
import com.madmike.opatr.server.OpenPartiesAndTrades;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class OpenPartiesAndTradesClient implements ClientModInitializer {

    private static KeyBinding openTradeKeybind;

    @Override
    public void onInitializeClient() {
        OpenPartiesAndTrades.LOGGER.info("Client initialized");

        SyncOffersReceiver.register();

        PartyLeaveMonitor.register();

        // Register the keybind
        openTradeKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.open_trading_screen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_T, // default key
                "category.open_parties_and_trades"
        ));

        // Register client tick event to check for the key press
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openTradeKeybind.wasPressed()) {
                client.setScreen(new TradingScreen());
            }
        });
    }
}