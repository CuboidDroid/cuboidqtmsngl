package com.cuboiddroid.cuboidqs.datagen.client;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, CuboidQuantumSingularitiesMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
