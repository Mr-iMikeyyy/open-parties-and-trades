package com.madmike.opatr.server.command;

import com.madmike.opatr.server.data.KnownParty;
import com.madmike.opatr.server.data.KnownPartyStorage;
import com.madmike.opatr.server.packets.offers.SyncAddOfferS2CPacket;
import com.madmike.opatr.server.data.TradeOffer;
import com.madmike.opatr.server.packets.party.SyncPartyS2CPacket;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.parties.party.api.IServerPartyAPI;
import com.madmike.opatr.server.data.TradeOfferStorage;


import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SellCommand {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("sell")
                    .then(argument("price", LongArgumentType.longArg(1))
                            .executes(ctx -> {
                                if (ctx.getSource().getEntity() instanceof ServerPlayerEntity player) {
                                    long price = LongArgumentType.getLong(ctx, "price");
                                    return handleSellCommand(player, price, player.getServer());
                                } else {
                                    ctx.getSource().sendError(Text.literal("Only players can use /sell."));
                                }
                                return 1;
                            })
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

        IServerPartyAPI party = OpenPACServerAPI.get(server).getPartyManager().getPartyByMember(player.getUuid());
        if (party != null) {
            KnownPartyStorage ps = KnownPartyStorage.get(player.getServerWorld());
            if (!ps.getKnownParties().containsKey(party.getId())) {
                KnownParty newParty = new KnownParty(party.getId(), party.getDefaultName());
                ps.addOrUpdateParty(newParty);
                SyncPartyS2CPacket.sendToAll(newParty, server);
            }
        }


        ItemStack listedItem = stack.copy();
        player.getMainHandStack().setCount(0);// remove the item

        TradeOffer offer = new TradeOffer(
                UUID.randomUUID(),
                player.getUuid(),
                listedItem,
                price,
                (party == null ? null : party.getId())
        );

        TradeOfferStorage.get(player.getServerWorld()).addOffer(offer);

        SyncAddOfferS2CPacket.sendToAll(offer, server);

        player.sendMessage(Text.literal("Listed item for " + price + " coins.").formatted(Formatting.GOLD), false);
        return 1;
    }
}
