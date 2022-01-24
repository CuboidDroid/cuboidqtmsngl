package com.cuboiddroid.cuboidqs.modules.collapser.recipe;

import com.cuboiddroid.cuboidqs.Config;
import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.tile.QuantumCollapserTileEntityBase;
import com.cuboiddroid.cuboidqs.setup.ModBlocks;
import com.cuboiddroid.cuboidqs.setup.ModRecipeSerializers;
import com.cuboiddroid.cuboidqs.setup.ModRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/*
  Recipes for collapsing items into singularities for the Quantum Singularity Collapser
 */
public class QuantumCollapsingRecipe implements IRecipe<IInventory> {
    private final ResourceLocation recipeId;
    private Ingredient ingredient;
    private String input;
    private ItemStack result;
    private int inputAmount;
    private int workTicks;
    private int tier;

    public QuantumCollapsingRecipe(ResourceLocation recipeId) {
        this.recipeId = recipeId;
        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        //CuboidQuantumSingularitiesMod.LOGGER.info("---> Loaded " + this.toString());
    }

    protected QuantumCollapsingRecipe(ResourceLocation recipeId, String input, Ingredient ingredient, ItemStack result, int inputAmount, int workTicks, int tier)
    {
        this.recipeId = recipeId;
        this.input = input;
        this.ingredient = ingredient;
        this.result = result;
        this.inputAmount = inputAmount;
        this.workTicks = workTicks;
        this.tier = tier;

        // This output is not required, but it can be used to detect when a recipe has been
        // loaded into the game.
        //CuboidQuantumSingularitiesMod.LOGGER.info("---> Loaded " + this.toString());
    }

    /*
    @Override
    public String toString() {

        // Overriding toString is not required, it's just useful for debugging.
        return "QuantumCollapsingRecipe [ingredient=" + this.ingredient +
                ", result=" + this.result + ", id=" + this.recipeId + "]";
    }
    */

    /**
     * Get the input ingredient
     *
     * @return The input ingredient
     */
    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public String getInput() {
        return this.input;
    }

    /**
     * Get the time in ticks to recycle the input ingredient
     *
     * @return The work time in ticks
     */
    public int getWorkTicks() {
        return this.workTicks;
    }

    /**
     * Get the tier of collapser required for the singularity to be generated
     *
     * @return The tier where 1 = iron and 5 = netherite
     */
    public int getTier() {
        return this.tier;
    }

    /**
     * Get the energy in FE needed to recycle the input ingredient
     *
     * @return The energy required in FE
     */
    public int getRequiredInputAmount() {
        return this.inputAmount;
    }

    /**
     * Get the Iron Quantum Collapser image as the toast symbol
     *
     * @return
     */
    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.IRON_QUANTUM_COLLAPSER.get());
    }

    /**
     * Get the recipe's resource location (recipe ID)
     *
     * @return the recipe ID as a ResourceLocation
     */
    @Override
    public ResourceLocation getId() {
        return this.recipeId;
    }

    /**
     * Get the recipe serializer
     *
     * @return the IRecipeSerializer for the CollapsingRecipe
     */
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COLLAPSING.get();
    }

    /**
     * Get the recipe type for the CollapsingRecipe
     *
     * @return The IRecipeType for this recipe
     */
    public IRecipeType<?> getType() {
        return ModRecipeTypes.COLLAPSING;
    }

    /**
     * Checks if there is a recipe match for the ingredient in the tile entities input slot
     *
     * @param inv   The collapser tile entity
     * @param level The level / world
     * @return true if there is a match, otherwise false
     */
    @Override
    public boolean matches(IInventory inv, World level) {
        return this.ingredient.test(inv.getItem(QuantumCollapserTileEntityBase.INPUT_SLOT));
    }

    /**
     * Assemble the recipe and return the result item stack
     *
     * @param inventory
     * @return
     */
    @Override
    public ItemStack assemble(IInventory inventory) {
        return this.getResultItem();
    }

    /**
     * Checks if the recipe can fit in the machine. As this recipe is for single input, we'll just say yes!
     *
     * @param gridWidth
     * @param gridHeight
     * @return always returns true
     */
    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return true;
    }

    /**
     * Gets the result item.
     *
     * @return
     */
    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    /**
     * Indicates that this recipe has special processing to Forge/Minecraft
     *
     * @return
     */
    @Override
    public boolean isSpecial() {
        return true;
    }

    // ---- Serializer ----

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<QuantumCollapsingRecipe> {

        @Override
        public QuantumCollapsingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            QuantumCollapsingRecipe recipe = new QuantumCollapsingRecipe(recipeId);
            recipe.workTicks = JSONUtils.getAsInt(json, "work_ticks", Config.defaultRecipeTicks.get());

            JsonObject inputJson = JSONUtils.getAsJsonObject(json, "input");
            if (inputJson.has("item")) {
                recipe.input = JSONUtils.getAsString(inputJson, "item");
                ResourceLocation inputItemId = new ResourceLocation(JSONUtils.getAsString(inputJson, "item"));

                recipe.ingredient = Ingredient.of(new ItemStack(ForgeRegistries.ITEMS.getValue(inputItemId), 1));
            } else {
                try {
                    recipe.ingredient = Ingredient.fromJson(inputJson);
                    recipe.input = "#" + JSONUtils.getAsString(inputJson, "tag");
                } catch (Exception ex) {
                    CuboidQuantumSingularitiesMod.LOGGER.warn("Could not load tag: '" + JSONUtils.getAsString(inputJson, "tag") + "' - recipe: '" + recipeId.getPath() + "'\n\n" + ex);
                    recipe.ingredient = Ingredient.EMPTY;
                }
            }

            recipe.inputAmount = JSONUtils.getAsInt(inputJson, "count", 1024);

            JsonObject resultJson = JSONUtils.getAsJsonObject(json, "result");
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(resultJson, "item"));
            int resultCount = JSONUtils.getAsInt(resultJson, "count", 1);

            recipe.result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), resultCount);

            recipe.tier = JSONUtils.getAsInt(inputJson, "tier", 1);

            return recipe;
        }

        @Nullable
        @Override
        public QuantumCollapsingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            QuantumCollapsingRecipe recipe = new QuantumCollapsingRecipe(recipeId);
            recipe.workTicks = buffer.readVarInt();
            recipe.ingredient = Ingredient.fromNetwork(buffer);
            recipe.result = buffer.readItem();
            recipe.inputAmount = buffer.readVarInt();
            recipe.tier = buffer.readVarInt();

            return recipe;
        }

        @Override
        public void toNetwork(PacketBuffer buffer, QuantumCollapsingRecipe recipe) {
            buffer.writeVarInt(recipe.workTicks);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.inputAmount);
            buffer.writeVarInt(recipe.tier);
        }
    }
}
