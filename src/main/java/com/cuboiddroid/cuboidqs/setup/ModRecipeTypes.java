package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.recipe.QuantumCollapsingRecipe;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipeTypes {
    public static final IRecipeType<QuantumCollapsingRecipe> COLLAPSING =
            IRecipeType.register(CuboidQuantumSingularitiesMod.MOD_ID + ":collapsing");

    static void register() {}
}
