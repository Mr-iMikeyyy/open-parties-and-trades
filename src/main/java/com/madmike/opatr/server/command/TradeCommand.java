package com.madmike.opatr.server.command;

import com.madmike.opatr.server.gui.TradingScreenHandler;
import com.madmike.opatr.server.trades.TradeOffer;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xaero.pac.common.parties.party.api.IPartyAPI;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;
import com.madmike.opatr.server.data.OfferStorage;


import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TradeCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("trade")
                    // Base /trade opens the GUI
                    .executes(context -> {
                        ServerCommandSource source = context.getSource();

                        if (source.getEntity() instanceof ServerPlayerEntity player) {
                            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                                    (syncId, inventory, p) -> new TradingScreenHandler(syncId, inventory),
                                    Text.literal("Trading Terminal")
                            ));
                        } else {
                            source.sendError(Text.literal("Only players can use /trade."));
                        }

                        return 1;
                    })

                    // /trade sell [price]
                    .then(literal("sell")
                            .then(argument("price", LongArgumentType.longArg(1))
                                    .executes(ctx -> {
                                        if (ctx.getSource().getEntity() instanceof ServerPlayerEntity player) {
                                            long price = LongArgumentType.getLong(ctx, "price");
                                            return handleSellCommand(player, price, player.getServer());
                                        } else {
                                            ctx.getSource().sendError(Text.literal("Only players can use /trade."));
                                        }
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }

    private static int handleSellCommand(ServerPlayerEntity player, long price, MinecraftServer server) {
        ItemStack stack = player.getMainHandStack();

        if (stack.isEmpty()) {
            player.sendMessage(Text.literal("You're not holding any item to sell.").formatted(Formatting.RED), false);
            return 0;
        }

        // Require party membership
        IServerPartyAPI party = OpenPACServerAPI.get(server).getPartyManager().getPartyByMember(player.getUuid());
        if (party == null) {
            player.sendMessage(Text.literal("You must be in a party to sell items.").formatted(Formatting.RED), false);
            return 0;
        }

        ItemStack listedItem = stack.copy();
        player.getInventory().removeOne(stack); // remove 1 item

        TradeOffer offer = new TradeOffer(
                player.getUuid(),
                listedItem,
                price,
                party.getId()
        );

        OfferStorage.get(player.getServer().getOverworld()).addOffer(offer);

        player.sendMessage(Text.literal("✔ Listed item for " + price + " coins.").formatted(Formatting.GOLD), false);
        return 1;
    }
}
