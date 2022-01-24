package com.cuboiddroid.cuboidqs.datagen.server.recipes;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.datagen.server.ModRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;

public abstract class DataGenRecipesBase {

    protected static ResourceLocation modId(String path) {
        return new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, path);
    }

    protected static void smeltBlockToItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Block> block, RegistryObject<Item> item, String name) {
        CookingRecipeBuilder.smelting(Ingredient.of(block.get()), item.get(), 0.7f, 200)
                .unlockedBy("has_item", provider.hasItem(block.get()))
                .save(consumer, modId(name + "_smelting"));

        CookingRecipeBuilder.blasting(Ingredient.of(block.get()), item.get(), 0.7f, 100)
                .unlockedBy("has_item", provider.hasItem(block.get()))
                .save(consumer, modId(name + "_blasting"));
    }

    protected static void smeltBlockToItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block block, Item item, String name) {
        CookingRecipeBuilder.smelting(Ingredient.of(block), item, 0.7f, 200)
                .unlockedBy("has_item", provider.hasItem(block))
                .save(consumer, modId(name + "_smelting"));

        CookingRecipeBuilder.blasting(Ingredient.of(block), item, 0.7f, 100)
                .unlockedBy("has_item", provider.hasItem(block))
                .save(consumer, modId(name + "_blasting"));
    }

    protected static void smeltItemToItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> chunk, RegistryObject<Item> ingot, String name) {
        CookingRecipeBuilder.smelting(Ingredient.of(chunk.get()), ingot.get(), 0.7f, 200)
                .unlockedBy("has_item", provider.hasItem(chunk.get()))
                .save(consumer, modId(name + "_smelting"));

        CookingRecipeBuilder.blasting(Ingredient.of(chunk.get()), ingot.get(), 0.7f, 100)
                .unlockedBy("has_item", provider.hasItem(chunk.get()))
                .save(consumer, modId(name + "_blasting"));
    }

    protected static void craftBlock3x3(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Block> block, RegistryObject<Item> item, String name) {
        ShapedRecipeBuilder.shaped(block.get())
                .define('#', item.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", provider.hasItem(item.get()))
                .save(consumer, modId(name));
    }

    protected static void craftBlock2x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block block, Item item, String name) {
        craftBlock2x2(provider, consumer, block, item, name, 1);
    }

    protected static void craftBlock2x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block block, Item item, String name, int count) {
        ShapedRecipeBuilder.shaped(block, count)
                .define('#', item)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", provider.hasItem(item))
                .save(consumer, modId(name));
    }

    protected static void craftBlock2x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block outputBlock, Block inputBlock, String name) {
        craftBlock2x2(provider, consumer, outputBlock, inputBlock, name, 1);
    }

    protected static void craftBlock2x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block outputBlock, Block inputBlock, String name, int count) {
        ShapedRecipeBuilder.shaped(outputBlock, count)
                .define('#', inputBlock)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", provider.hasItem(inputBlock))
                .save(consumer, modId(name));
    }

    protected static void craftItemsFromBlock(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> ingot, int amount, RegistryObject<Block> block, String name) {
        ShapelessRecipeBuilder.shapeless(ingot.get(), amount)
                .requires(block.get())
                .unlockedBy("has_item", provider.hasItem(ingot.get()))
                .save(consumer, modId(name));
    }

    protected static void craftItemFromItems3x3(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> ingot, RegistryObject<Item> nugget, String name) {
        craftItemFromItems3x3(provider, consumer, ingot.get(), nugget.get(), name);
    }

    protected static void craftItemFromItems3x3(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Item ingot, RegistryObject<Item> nugget, String name) {
        craftItemFromItems3x3(provider, consumer, ingot, nugget.get(), name);
    }

    protected static void craftItemFromItems3x3(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> ingot, Item nugget, String name) {
        craftItemFromItems3x3(provider, consumer, ingot.get(), nugget, name);
    }

    protected static void craftItemFromItems3x3(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Item ingot, Item nugget, String name) {
        ShapedRecipeBuilder.shaped(ingot)
                .define('#', nugget)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", provider.hasItem(nugget))
                .save(consumer, modId(name));
    }

    protected static void craftItemsFromItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> nugget, RegistryObject<Item> ingot, String name, int count) {
        craftItemsFromItem(provider, consumer, nugget.get(), ingot.get(), name, count);
    }

    protected static void craftItemsFromItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Item nugget, RegistryObject<Item> ingot, String name, int count) {
        craftItemsFromItem(provider, consumer, nugget, ingot.get(), name, count);
    }

    protected static void craftItemsFromItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> nugget, Item ingot, String name, int count) {
        craftItemsFromItem(provider, consumer, nugget.get(), ingot, name, count);
    }

    protected static void craftItemsFromItem(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Item nugget, Item ingot, String name, int count) {
        ShapelessRecipeBuilder.shapeless(nugget, count)
                .requires(ingot)
                .unlockedBy("has_item", provider.hasItem(ingot))
                .save(consumer, modId(name));
    }

    protected static void craftItemFromItems2x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Item chunk, Item piece, String name) {
        ShapedRecipeBuilder.shaped(chunk)
                .define('#', piece)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", provider.hasItem(piece))
                .save(consumer, modId(name));
    }

    protected static void craftItemFromItems2x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> chunk, RegistryObject<Item> piece, String name) {
        craftItemFromItems2x2(provider, consumer, chunk.get(), piece.get(), name);
    }

    protected static void craftItemFromItems1x2(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, RegistryObject<Item> chunk, RegistryObject<Item> piece, String name) {
        ShapedRecipeBuilder.shaped(chunk.get())
                .define('#', piece.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_item", provider.hasItem(piece.get()))
                .save(consumer, modId(name));
    }

    protected static void stonecutterBlockFromBlock(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block output, Block input, String name) {
        stonecutterBlockFromBlock(provider, consumer, output, input, name, 1);
    }

    protected static void stonecutterBlockFromBlock(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, Block output, Block input, String name, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), output)
                .unlocks("has_item", provider.hasItem(input))
                .save(consumer, modId(name));

    }

}
