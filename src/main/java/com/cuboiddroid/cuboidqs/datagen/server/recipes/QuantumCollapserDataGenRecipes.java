package com.cuboiddroid.cuboidqs.datagen.server.recipes;

import com.cuboiddroid.cuboidqs.datagen.server.ModRecipeProvider;
import com.cuboiddroid.cuboidqs.modules.collapser.block.QuantumCollapserBlockBase;
import com.cuboiddroid.cuboidqs.setup.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class QuantumCollapserDataGenRecipes extends DataGenRecipesBase {
    public static void build(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer) {
        quantumCollapserFromBlocksAndFurnace(provider, consumer,
                ModBlocks.IRON_QUANTUM_COLLAPSER.get(),
                Blocks.IRON_BLOCK,
                "iron");

        quantumCollapserFromBlocksAndFurnace(provider, consumer,
                ModBlocks.GOLD_QUANTUM_COLLAPSER.get(),
                Blocks.GOLD_BLOCK,
                "gold");

        quantumCollapserFromBlocksAndFurnace(provider, consumer,
                ModBlocks.DIAMOND_QUANTUM_COLLAPSER.get(),
                Blocks.DIAMOND_BLOCK,
                "diamond");

        quantumCollapserFromBlocksAndFurnace(provider, consumer,
                ModBlocks.EMERALD_QUANTUM_COLLAPSER.get(),
                Blocks.EMERALD_BLOCK,
                "emerald");

        quantumCollapserFromBlocksAndFurnace(provider, consumer,
                ModBlocks.NETHERITE_QUANTUM_COLLAPSER.get(),
                Blocks.NETHERITE_BLOCK,
                "netherite");

        // upgrades
        quantumCollapserUpgrade(provider, consumer,
                ModBlocks.GOLD_QUANTUM_COLLAPSER.get(),
                ModBlocks.IRON_QUANTUM_COLLAPSER.get(),
                Blocks.GOLD_BLOCK,
                "gold");

        quantumCollapserUpgrade(provider, consumer,
                ModBlocks.DIAMOND_QUANTUM_COLLAPSER.get(),
                ModBlocks.GOLD_QUANTUM_COLLAPSER.get(),
                Blocks.DIAMOND_BLOCK,
                "diamond");

        quantumCollapserUpgrade(provider, consumer,
                ModBlocks.EMERALD_QUANTUM_COLLAPSER.get(),
                ModBlocks.DIAMOND_QUANTUM_COLLAPSER.get(),
                Blocks.EMERALD_BLOCK,
                "emerald");

        quantumCollapserUpgrade(provider, consumer,
                ModBlocks.NETHERITE_QUANTUM_COLLAPSER.get(),
                ModBlocks.EMERALD_QUANTUM_COLLAPSER.get(),
                Blocks.NETHERITE_BLOCK,
                "netherite");
    }

    private static void quantumCollapserFromBlocksAndFurnace(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, QuantumCollapserBlockBase output, Block block, String materialName) {
        ShapedRecipeBuilder.shaped(output)
                .define('#', block)
                .define('$', Blocks.BLAST_FURNACE)
                .pattern("###")
                .pattern("#$#")
                .pattern("###")
                .unlockedBy("has_item", provider.hasItem(Blocks.BLAST_FURNACE))
                .save(consumer, modId(materialName + "_quantum_collapser_from_blocks_and_blast_furnace"));
    }

    private static void quantumCollapserUpgrade(ModRecipeProvider provider, Consumer<IFinishedRecipe> consumer, QuantumCollapserBlockBase output, QuantumCollapserBlockBase prevTier, Block block, String materialName) {
        ShapedRecipeBuilder.shaped(output)
                .define('#', block)
                .define('$', Blocks.BLAST_FURNACE)
                .define('@', prevTier)
                .pattern(" $ ")
                .pattern("#@#")
                .pattern(" # ")
                .unlockedBy("has_item", provider.hasItem(Blocks.BLAST_FURNACE))
                .save(consumer, modId(materialName + "_quantum_collapser_upgrade"));
    }

}
