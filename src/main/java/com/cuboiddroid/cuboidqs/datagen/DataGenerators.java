package com.cuboiddroid.cuboidqs.datagen;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.datagen.client.ModBlockStateProvider;
import com.cuboiddroid.cuboidqs.datagen.client.ModItemModelProvider;
import com.cuboiddroid.cuboidqs.datagen.server.ModBlockTagsProvider;
import com.cuboiddroid.cuboidqs.datagen.server.ModItemTagsProvider;
import com.cuboiddroid.cuboidqs.datagen.server.ModLootTableProvider;
import com.cuboiddroid.cuboidqs.datagen.server.ModRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CuboidQuantumSingularitiesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
            gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
        }

        if (event.includeServer()) {
            ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, existingFileHelper);
            gen.addProvider(blockTags);
            gen.addProvider(new ModItemTagsProvider(gen, blockTags, existingFileHelper));

            gen.addProvider(new ModLootTableProvider(gen));
            gen.addProvider(new ModRecipeProvider(gen));
        }
    }
}
