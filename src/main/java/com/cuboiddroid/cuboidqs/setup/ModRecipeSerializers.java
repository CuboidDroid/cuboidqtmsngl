package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.modules.collapser.recipe.QuantumCollapsingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class ModRecipeSerializers {

    public static final RegistryObject<IRecipeSerializer<?>> COLLAPSING =
            Registration.RECIPE_SERIALIZERS.register("collapsing",
                    QuantumCollapsingRecipe.Serializer::new);

    static void register() {}
}
