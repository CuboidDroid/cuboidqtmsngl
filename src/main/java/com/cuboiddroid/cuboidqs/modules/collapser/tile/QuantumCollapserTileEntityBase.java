package com.cuboiddroid.cuboidqs.modules.collapser.tile;

import com.cuboiddroid.cuboidqs.modules.collapser.recipe.QuantumCollapsingRecipe;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityUtils;
import com.cuboiddroid.cuboidqs.setup.ModRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;

public abstract class QuantumCollapserTileEntityBase extends TileEntity implements ITickableTileEntity {
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    private static final int INPUT_SLOTS = 1;
    private static final int OUTPUT_SLOTS = 1;
    public static final int TOTAL_SLOTS = INPUT_SLOTS + OUTPUT_SLOTS;

    private ItemStackHandler inputItemHandler = createInputHandler();
    private ItemStackHandler outputItemHandler = createOutputHandler();
    private CombinedInvWrapper combinedItemHandler = new CombinedInvWrapper(inputItemHandler, outputItemHandler);

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> inputItemHandler);
    private LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private LazyOptional<IItemHandler> combinedHandler = LazyOptional.of(() -> combinedItemHandler);

    private final float speedFactor;
    private final int tier;

    private int processingTime = 0;
    private int recipeTime = -1;
    private int amountConsumed = 0;
    private int amountRequired = -1;
    private Ingredient currentIngredient = Ingredient.EMPTY;
    private String currentInput = null;
    private ItemStack currentOutput = ItemStack.EMPTY;

    private QuantumCollapsingRecipe cachedRecipe = null;

    public QuantumCollapserTileEntityBase(TileEntityType<?> tileEntityType, float speedFactor, int tier) {
        super(tileEntityType);
        this.speedFactor = speedFactor;
        this.tier = tier;
    }

    public abstract ITextComponent getDisplayName();

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide)
            return;

        boolean didWorkThisTick = false;

        QuantumCollapsingRecipe recipe = getRecipe();

        // bug out early if the collapser tier is too low
        if (recipe != null && this.tier < recipe.getTier())
            return;

        if (recipe != null) {
            if (this.currentIngredient.isEmpty()) {
                this.currentIngredient = recipe.getIngredient();
                this.currentInput = recipe.getInput();
                this.amountRequired = recipe.getRequiredInputAmount();
                this.recipeTime = (int) (recipe.getWorkTicks() / this.speedFactor);
                this.currentOutput = recipe.getResultItem();
                setChanged();
            } else if (!currentIngredient.test(this.inputItemHandler.getStackInSlot(0))) {
                // can't change recipe mid-load, but if already filled up, let it run
                if (amountConsumed < amountRequired)
                  return;
            }
        }

        if (!currentIngredient.isEmpty()) {
            if (processingTime <= 0) {
                ItemStack currentInputStack = inputItemHandler.getStackInSlot(INPUT_SLOT);
                if (currentInputStack.isEmpty() || !currentIngredient.test(currentInputStack))
                    // nothing in the input slot or not the right item
                {
                    // if we haven't already filled up with the right item, bug out!
                    if (amountConsumed < amountRequired)
                      return;
                } else if (amountConsumed < amountRequired) {
                    // consume the  amount of input items
                    int amountNeeded = amountRequired - amountConsumed;

                    if (amountNeeded > 0) {
                        int amountToConsume = Math.min(amountNeeded, currentInputStack.getCount());

                        ItemStack newInputStack = ItemStack.EMPTY;
                        if (amountToConsume < currentInputStack.getCount()) {
                            newInputStack = currentInputStack.copy();
                            newInputStack.shrink(amountToConsume);
                        }
                        inputItemHandler.setStackInSlot(INPUT_SLOT, newInputStack);
                        amountConsumed += amountToConsume;

                        // force a block update to be sent to the client so that it updates the GUI
                        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2);
                        setChanged();
                    }
                }
            }

            if (recipe != null && processingTime <= 0 && amountConsumed >= amountRequired) {
                // we've consumed enough items but have not started working - start working if possible!
                ItemStack outputSlot = this.outputItemHandler.getStackInSlot(0);
                if (outputSlot.isEmpty() || (!currentOutput.isEmpty() &&
                        outputSlot.sameItem(currentOutput) && currentOutput.getTag().get("Id") == outputSlot.getTag().get("Id")
                )) {
                    this.recipeTime = (int) (recipe.getWorkTicks() / this.speedFactor);
                    this.processingTime = this.recipeTime;
                    setChanged();
                }
            }

            if (processingTime > 0 && amountConsumed >= amountRequired) {
                didWorkThisTick = doWork();
                setChanged();
            }
        }

        BlockState blockState = this.level.getBlockState(this.worldPosition);
        if (blockState.getValue(BlockStateProperties.LIT) != (processingTime > 0 || didWorkThisTick)) {
            this.level.setBlock(this.worldPosition, blockState.setValue(BlockStateProperties.LIT, processingTime > 0),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    private boolean doWork() {
        assert this.level != null;

        boolean hasRoomForOutputs = false;
        int outputSlotIndex = 0;
        while (!hasRoomForOutputs && outputSlotIndex < OUTPUT_SLOTS) {
            ItemStack outputSlot = this.outputItemHandler.getStackInSlot(outputSlotIndex);
            hasRoomForOutputs = outputSlot.isEmpty() || (!currentOutput.isEmpty() &&
                    currentOutput.sameItem(outputSlot) &&
                    currentOutput.getTag().get("Id") == outputSlot.getTag().get("Id") &&
                    currentOutput.getCount() + outputSlot.getCount() <= currentOutput.getMaxStackSize());

            outputSlotIndex++;
        }

        if (!hasRoomForOutputs) {
            // don't stop - enter a "holding pattern" instead until the current output slot is cleared
            stopWork();
            return false;
        }

        if (processingTime > 0)
            processingTime--;

        if (processingTime <= 0) {
            finishWork();

            // return true so that we don't get "flicker" for the core between recipes
            return true;
        }

        return false;
    }

    private void stopWork() {
        this.processingTime = 0;
        this.recipeTime = -1;
        setChanged();
    }

    private void finishWork() {
        moveStackToOutputs(currentOutput);

        this.amountConsumed = 0;
        this.processingTime = 0;
        this.recipeTime = -1;
        this.amountRequired = -1;
        this.currentOutput = ItemStack.EMPTY;
        this.currentIngredient = Ingredient.EMPTY;
        this.currentInput = null;
        setChanged();
    }

    private void moveStackToOutputs(ItemStack stack) {
        int firstEmptyIndex = -1;
        for (int outputSlotIndex = 0; outputSlotIndex < OUTPUT_SLOTS; outputSlotIndex++) {
            ItemStack outputStack = outputItemHandler.getStackInSlot(outputSlotIndex).copy();
            if (outputStack.sameItem(stack) && outputStack.getCount() + stack.getCount() <= outputStack.getMaxStackSize()) {
                outputStack.grow(stack.getCount());
                outputItemHandler.setStackInSlot(outputSlotIndex, outputStack);
                return;
            }

            if (outputStack.isEmpty() && firstEmptyIndex < 0) {
                firstEmptyIndex = outputSlotIndex;
            }
        }

        if (firstEmptyIndex >= 0) {
            outputItemHandler.setStackInSlot(firstEmptyIndex, stack.copy());
        } else {
            throw new IllegalStateException("Attempted to move stack to output slots without checking if there is room first!");
        }
    }

    @Nullable
    public QuantumCollapsingRecipe getRecipe() {
        if (this.level == null)
            return null;

        if (this.inputItemHandler.getStackInSlot(0).isEmpty()) {
            if (this.currentIngredient.isEmpty())
                return null;
        }

        // make an inventory - from current ingredient if input slot is empty, otherwise from whatever is in input slot
        IInventory inv = this.inputItemHandler.getStackInSlot(0).isEmpty()
                ? new Inventory(this.currentIngredient.getItems()[0].copy())
                : getInputsAsInventory();

        if (cachedRecipe == null || !cachedRecipe.matches(inv, this.level)) {
            cachedRecipe = this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.COLLAPSING, inv, this.level).orElse(null);
        }

        return cachedRecipe;
    }

    private Inventory getInputsAsInventory() {
        return new Inventory(this.inputItemHandler.getStackInSlot(0).copy());
    }

    private ItemStack getWorkOutput(@Nullable QuantumCollapsingRecipe recipe) {
        if (recipe != null) {
            return recipe.getResultItem().copy();
        }

        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            // if side is null, then it's not via automation, so provide access to everything
            if (side == null)
                return combinedHandler.cast();

            // if side is not null, then it's automation
            // BOTTOM = output, UP = base item input, sides = additional
            switch (side) {
                case UP:
                    return inputHandler.cast();

                default:
                    return outputHandler.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        inputItemHandler.deserializeNBT(tag.getCompound("invIn"));
        outputItemHandler.deserializeNBT(tag.getCompound("invOut"));
        processingTime = tag.getInt("procTime");
        recipeTime = tag.getInt("recTime");
        amountConsumed = tag.getInt("amtConsumed");
        amountRequired = tag.getInt("amtRequired");

        try {
            String curIngId = tag.getString("curIng");

            if (curIngId != null && !curIngId.isEmpty()) {
                if (curIngId.startsWith("#")) {
                    ResourceLocation tagLocation = new ResourceLocation(curIngId.substring(1));
                    ITag<Item> inputTag = TagCollectionManager.getInstance().getItems().getTag(tagLocation);
                    if (inputTag != null) {
                        currentIngredient = Ingredient.of(inputTag);
                    }
                } else {
                    ResourceLocation itemId = new ResourceLocation(curIngId);
                    currentIngredient = Ingredient.of(new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), 1));
                }
            } else
                currentIngredient = Ingredient.EMPTY;
        } catch(Exception ex) {
            currentIngredient = Ingredient.EMPTY;
        }

        String currentOutputId = tag.getString("curOutId");

        QuantumSingularity qs = QuantumSingularityRegistry.getInstance().getSingularityById(new ResourceLocation(currentOutputId));
        currentOutput = qs == null
                ? ItemStack.EMPTY
                : QuantumSingularityUtils.getItemForSingularity(qs);

        //currentOutput = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(currentOutputId)));

        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("invIn", inputItemHandler.serializeNBT());
        tag.put("invOut", outputItemHandler.serializeNBT());
        tag.putInt("procTime", processingTime);
        tag.putInt("recTime", recipeTime);
        tag.putInt("amtConsumed", amountConsumed);
        tag.putInt("amtRequired", amountRequired);

        tag.putString("curIng", currentInput == null ? "" : currentInput);

        QuantumSingularity qs = QuantumSingularityUtils.getSingularity(currentOutput);

        tag.putString("curOutId", qs == null ? "" : qs.getId().toString());

        return super.save(tag);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
        outputHandler.invalidate();
        combinedHandler.invalidate();
    }

    /**
     * When the world loads from disk, the server needs to send the TileEntity information to the client
     * It uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
     * getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
     * getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
     *
     * @return the packet
     */
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        this.save(nbtTag);
        this.setChanged();
        return new SUpdateTileEntityPacket(getBlockPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        this.load(level.getBlockState(worldPosition), tag);
        this.setChanged();
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 2);
    }

    /* Creates a tag containing all of the TileEntity information, used by vanilla to transmit from server to client
     */
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        save(nbtTagCompound);
        return nbtTagCompound;
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     */
    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        this.load(blockState, parentNBTTagCompound);
    }

    private ItemStackHandler createInputHandler() {
        return new ItemStackHandler(INPUT_SLOTS) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return this.getStackInSlot(slot).isEmpty() || this.getStackInSlot(slot).sameItem(stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(OUTPUT_SLOTS) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                // can't insert into the output slot
                return stack;
            }
        };
    }

    public abstract Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity);

    public int getProcessingTime() {
        return this.processingTime;
    }

    public void setProcessingTime(int value) {
        this.processingTime = value;
    }

    public int getRecipeTime() {
        return this.recipeTime == 0 ? -1 : this.recipeTime;
    }

    public void setRecipeTime(int value) {
        this.recipeTime = value <= 0 ? -1 : value;
    }

    public int getAmountConsumed() {
        return this.amountConsumed;
    }

    public void setAmountConsumed(int value) {
        this.amountConsumed = value;
    }

    public int getAmountRequired() {
        return this.amountRequired == 0 ? -1 : this.amountRequired;
    }

    public void setAmountRequired(int value) {
        this.amountRequired = value;
    }

    public ItemStack getCollapsingItemStackForDisplay() {
        if (this.currentOutput.isEmpty())
            return ItemStack.EMPTY;

        QuantumSingularity qs = QuantumSingularityUtils.getSingularity(this.currentOutput);

        Ingredient inputIngredient = qs.getIngredient();

        if (inputIngredient.isEmpty())
            return ItemStack.EMPTY;

        Date date = new Date();
        int index = (int)((date.getTime() / 1000) % inputIngredient.getItems().length);

        return inputIngredient.getItems()[index].copy();
    }

    public ItemStack getSingularityOutputForDisplay() {
        return this.currentOutput.copy();
    }
}
