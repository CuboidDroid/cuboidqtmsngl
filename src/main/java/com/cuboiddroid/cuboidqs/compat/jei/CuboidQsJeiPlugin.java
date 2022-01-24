package com.cuboiddroid.cuboidqs.compat.jei;

import com.cuboiddroid.cuboidqs.Config;
import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.inventory.*;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularity;
import com.cuboiddroid.cuboidqs.modules.collapser.registry.QuantumSingularityUtils;
import com.cuboiddroid.cuboidqs.modules.collapser.screen.*;
import com.cuboiddroid.cuboidqs.setup.ModBlocks;
import com.cuboiddroid.cuboidqs.setup.ModItems;
import com.cuboiddroid.cuboidqs.setup.ModRecipeTypes;
import com.cuboiddroid.cuboidqs.util.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class CuboidQsJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = CuboidQuantumSingularitiesMod.getModId("plugin/main");

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType) {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (Config.enableJeiPlugin.get()) {
            IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
            registration.addRecipeCategories(new CollapsingRecipeCategoryJei(guiHelper));
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Config.enableJeiPlugin.get()) {
            registration.addRecipes(getRecipesOfType(ModRecipeTypes.COLLAPSING), Constants.COLLAPSING);
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiClickArea.get()) {
            registration.addRecipeClickArea(IronQuantumCollapserScreen.class, 76, 41, 28, 21, Constants.COLLAPSING);
            registration.addRecipeClickArea(GoldQuantumCollapserScreen.class, 76, 41, 28, 21, Constants.COLLAPSING);
            registration.addRecipeClickArea(DiamondQuantumCollapserScreen.class, 76, 41, 28, 21, Constants.COLLAPSING);
            registration.addRecipeClickArea(EmeraldQuantumCollapserScreen.class, 76, 41, 28, 21, Constants.COLLAPSING);
            registration.addRecipeClickArea(NetheriteQuantumCollapserScreen.class, 76, 41, 28, 21, Constants.COLLAPSING);
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        if (Config.enableJeiPlugin.get()) {
            registration.addRecipeTransferHandler(IronQuantumCollapserContainer.class, Constants.COLLAPSING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(GoldQuantumCollapserContainer.class, Constants.COLLAPSING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(DiamondQuantumCollapserContainer.class, Constants.COLLAPSING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(EmeraldQuantumCollapserContainer.class, Constants.COLLAPSING, 0, 2, 2, 36);
            registration.addRecipeTransferHandler(NetheriteQuantumCollapserContainer.class, Constants.COLLAPSING, 0, 2, 2, 36);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (Config.enableJeiPlugin.get() && Config.enableJeiCatalysts.get()) {
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.IRON_QUANTUM_COLLAPSER.get()), Constants.COLLAPSING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.GOLD_QUANTUM_COLLAPSER.get()), Constants.COLLAPSING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.DIAMOND_QUANTUM_COLLAPSER.get()), Constants.COLLAPSING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMERALD_QUANTUM_COLLAPSER.get()), Constants.COLLAPSING);
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.NETHERITE_QUANTUM_COLLAPSER.get()), Constants.COLLAPSING);
        }
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        ModItems.QUANTUM_SINGULARITY.ifPresent(item -> {
            registration.registerSubtypeInterpreter(item, (ingredient, context) -> {
                if (context == UidContext.Ingredient && ingredient.hasTag())
                {
                    QuantumSingularity singularity = QuantumSingularityUtils.getSingularity(ingredient);
                    return singularity != null ? singularity.getId().toString() : IIngredientSubtypeInterpreter.NONE;
                }
                return IIngredientSubtypeInterpreter.NONE;
            });
/*
            registration.registerSubtypeInterpreter(item,  stack -> {
                QuantumSingularity singularity = QuantumSingularityUtils.getSingularity(stack);
                return singularity != null ? singularity.getId().toString() : "";
            });
*/
        });
    }
}
