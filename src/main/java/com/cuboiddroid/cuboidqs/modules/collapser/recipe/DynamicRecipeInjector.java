package com.cuboiddroid.cuboidqs.modules.collapser.recipe;

import com.cuboiddroid.cuboidqs.Config;
import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityRegistry;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public final class DynamicRecipeInjector implements IResourceManagerReloadListener {
    private static RecipeManager recipeManager;

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        QuantumSingularityRegistry.getInstance().getSingularities().forEach(singularity -> {
            QuantumCollapsingRecipe recipe = makeQuantumCollapsingRecipe(singularity);
            if (recipe != null) {
                getRecipeManager().recipes
                        .computeIfAbsent(recipe.getType(), t -> new HashMap<>())
                        .put(recipe.getId(), recipe);
            }
        });
    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        recipeManager = event.getDataPackRegistries().getRecipeManager();
        event.addListener(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRecipesUpdated(RecipesUpdatedEvent event) {
        recipeManager = event.getRecipeManager();
    }

    public static RecipeManager getRecipeManager() {
        if (recipeManager.recipes instanceof ImmutableMap) {
            recipeManager.recipes = new HashMap<>(recipeManager.recipes);
            recipeManager.recipes.replaceAll((t, v) -> new HashMap<>(recipeManager.recipes.get(t)));
        }

        return recipeManager;
    }

    private static QuantumCollapsingRecipe makeQuantumCollapsingRecipe(QuantumSingularity singularity) {
        Ingredient ingredient = singularity.getIngredient();
        if (ingredient == Ingredient.EMPTY)
            return null;

        ResourceLocation id = singularity.getId();
        ResourceLocation recipeId = new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, id.getPath() + "_dynamic_qs");
        ItemStack output = QuantumSingularityUtils.getItemForSingularity(singularity);
        int inputCount = singularity.getInputCount();
        int workTicks = singularity.getWorkTicks();
        int tier = singularity.getTier();

        return new QuantumCollapsingRecipe(recipeId, singularity.getInput(), ingredient, output, inputCount, workTicks, tier);
    }
}
