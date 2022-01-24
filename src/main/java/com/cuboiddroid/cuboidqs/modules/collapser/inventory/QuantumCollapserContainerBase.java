package com.cuboiddroid.cuboidqs.modules.collapser.inventory;

import com.cuboiddroid.cuboidqs.modules.collapser.tile.QuantumCollapserTileEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class QuantumCollapserContainerBase extends Container {

    protected final World level;
    protected QuantumCollapserTileEntityBase tileEntity;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;

    public QuantumCollapserContainerBase(
            ContainerType<?> tileEntityType,
            int windowId,
            World world,
            BlockPos pos,
            PlayerInventory playerInventory,
            PlayerEntity player) {
        super(tileEntityType, windowId);
        this.tileEntity = (QuantumCollapserTileEntityBase) world.getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.level = playerInventory.player.level;

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, QuantumCollapserTileEntityBase.INPUT_SLOT, 53, 43));
                addSlot(new SlotItemHandler(h, QuantumCollapserTileEntityBase.OUTPUT_SLOT, 112, 43));
            });
        }

        layoutPlayerInventorySlots(8, 84);
        trackData();
    }

    // Setup syncing of data from server to client so that the GUI can show the amount of items consumed in the block
    private void trackData() {
        // amount consumed
        this.addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getAmountConsumed();
            }

            @Override
            public void set(int value) {
                tileEntity.setAmountConsumed(value);
            }
        });

        // amount required
        this.addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getAmountRequired();
            }

            @Override
            public void set(int value) {
                tileEntity.setAmountRequired(value);
            }
        });

        // processing time
        this.addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getProcessingTime();
            }

            @Override
            public void set(int value) {
                tileEntity.setProcessingTime(value);
            }
        });

        // recipe time
        this.addDataSlot(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getRecipeTime();
            }

            @Override
            public void set(int value) {
                tileEntity.setRecipeTime(value);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getPos() {
        return this.tileEntity.getBlockPos();
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        int playerInventoryStartSlot = QuantumCollapserTileEntityBase.TOTAL_SLOTS;
        int playerInventoryEndSlot = playerInventoryStartSlot + 27;
        int playerHotbarStartSlot = playerInventoryEndSlot;
        int playerHotbarEndSlot = playerHotbarStartSlot + 9;

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < QuantumCollapserTileEntityBase.TOTAL_SLOTS) {
                // moving the inputs or output out into player inventory slots
                if (!this.moveItemStackTo(stack, playerInventoryStartSlot, playerHotbarEndSlot, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                // moving something in the player inventory
                if (!this.slots.get(0).hasItem() || this.slots.get(0).getItem().sameItem(stack)) {
                    // first input is empty or already contains this item - try move
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarStartSlot) {
                    // it's something we can't use, and the player
                    // clicked in their inventory, so try move to hotbar
                    if (!this.moveItemStackTo(stack, playerHotbarStartSlot, playerHotbarEndSlot, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarEndSlot) {
                    // it's something we can't use, and the player
                    // clicked in their hotbar, so try move to their
                    // inventory slots instead.
                    if (!this.moveItemStackTo(stack, playerInventoryStartSlot, playerHotbarStartSlot, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    @Override
    public abstract boolean stillValid(PlayerEntity player);

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
