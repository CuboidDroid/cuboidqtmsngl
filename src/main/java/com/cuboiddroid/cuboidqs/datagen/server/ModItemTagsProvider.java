package com.cuboiddroid.cuboidqs.datagen.server;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.setup.ModItems;
import com.cuboiddroid.cuboidqs.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagsProvider, CuboidQuantumSingularitiesMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
		tag(ModTags.Items.QUANTUM_SINGULARITIES).add(ModItems.QUANTUM_SINGULARITY.get());
    }
}
