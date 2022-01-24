package com.cuboiddroid.cuboidqs.datagen.server;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, CuboidQuantumSingularitiesMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
