package com.madmike.opatr.server;

import com.madmike.opatr.server.command.TradeCommand;
import com.madmike.opatr.server.net.ServerNetworking;
import com.madmike.opatr.server.screens.ModScreens;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenPartiesAndTrades implements ModInitializer {
	public static final String MOD_ID = "opatr";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Trading Mod...");

		// Register networking
		ServerNetworking.registerServerHandlers();

		// Register screen handlers
		ModScreens.registerScreenHandlers();

		// Register commands
		TradeCommand.registerCommands();
	}
}