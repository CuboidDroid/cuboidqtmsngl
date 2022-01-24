package com.cuboiddroid.cuboidqs.datagen.client;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CuboidQuantumSingularitiesMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        registerBlockItemModels();
    }

    private void registerBlockItemModels() {
        withExistingParent("iron_quantum_collapser", modLoc("block/iron_quantum_collapser"));
        withExistingParent("gold_quantum_collapser", modLoc("block/gold_quantum_collapser"));
        withExistingParent("diamond_quantum_collapser", modLoc("block/diamond_quantum_collapser"));
        withExistingParent("emerald_quantum_collapser", modLoc("block/emerald_quantum_collapser"));
        withExistingParent("netherite_quantum_collapser", modLoc("block/netherite_quantum_collapser"));
    }
}
