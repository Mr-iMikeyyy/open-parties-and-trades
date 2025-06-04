package com.madmike.opatr.server.gui;

import com.glisco.numismaticoverhaul.ModComponents;
import com.madmike.opatr.server.screens.ModScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class TradingScreenHandler extends ScreenHandler {
    private final SimpleInventory inventory;
    private final long coinBalance;
    private final SimpleInventory sellInventory = new SimpleInventory(1);

    public TradingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreens.TRADING_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(1); // 1 item listed for sale
        inventory.onOpen(playerInventory.player);

        // Get and store player's coin balance
        this.coinBalance = ModComponents.CURRENCY.get(playerInventory.player).getValue();

        // Slot for the item being listed
        this.addSlot(new Slot(sellInventory, 0, 80, 35)); // x/y are pixel coords in GUI

        // Player inventory slots (main)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar slots
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public ItemStack getListedItem() {
        return inventory.getStack(0);
    }

    public void setListedItem(ItemStack stack) {
        inventory.setStack(0, stack);
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    public long getCoinBalance() {
        return this.coinBalance;
    }

    public ItemStack getSellSlotStack() {
        return sellInventory.getStack(0);
    }
    public void clearSellSlot() {
        sellInventory.setStack(0, ItemStack.EMPTY);
    }
}
