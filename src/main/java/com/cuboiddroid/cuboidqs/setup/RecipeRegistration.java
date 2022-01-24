package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.recipe.QuantumCollapsingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CuboidQuantumSingularitiesMod.MOD_ID)
@Mod.EventBusSubscriber(modid = CuboidQuantumSingularitiesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistration {
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

        registry.register(new QuantumCollapsingRecipe.Serializer().setRegistryName(CuboidQuantumSingularitiesMod.MOD_ID, "collapsing"));
    }
}
