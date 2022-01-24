package com.cuboiddroid.cuboidqs.datagen.server;

import com.cuboiddroid.cuboidqs.datagen.server.recipes.*;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        QuantumCollapserDataGenRecipes.build(this, consumer);
    }

    public InventoryChangeTrigger.Instance hasItem(ITag<Item> itemITag) {
        return RecipeProvider.has(itemITag);
    }

    public InventoryChangeTrigger.Instance hasItem(IItemProvider itemProvider) {
        return RecipeProvider.has(itemProvider);
    }
}
