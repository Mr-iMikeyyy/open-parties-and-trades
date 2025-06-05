package com.madmike.opatr.server;

import com.madmike.opatr.server.command.TradeCommand;
import com.madmike.opatr.server.data.OfferStorage;
import com.madmike.opatr.server.events.EventManager;
import com.madmike.opatr.server.net.ServerNetworking;
import net.fabricmc.api.ModInitializer;

import net.minecraft.server.MinecraftServer;
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
		LOGGER.info("Initializing OPATR...");

		//Register Events
		EventManager.register();

		// Register networking
		ServerNetworking.registerServerHandlers();

		// Register commands
		TradeCommand.registerCommands();

		LOGGER.info("OPATR Initialized! Happy Trading!");
	}
}