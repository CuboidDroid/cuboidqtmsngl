package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final class Fluids {
        public static final ITag.INamedTag<Fluid> MINECRAFT_FLUID_WATER = minecraft("water");
        public static final ITag.INamedTag<Fluid> MINECRAFT_FLUID_LAVA = minecraft("lava");

        private static ITag.INamedTag<Fluid> forge(String path) {
            return FluidTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Fluid> minecraft(String path) {
            return FluidTags.bind(new ResourceLocation("minecraft", path).toString());
        }

        private static ITag.INamedTag<Fluid> mod(String path) {
            return FluidTags.bind(new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, path).toString());
        }
    }

    public static final class Blocks {
        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> minecraft(String path) {
            return BlockTags.bind(new ResourceLocation("minecraft", path).toString());
        }

        private static ITag.INamedTag<Block> mod(String path) {
            return BlockTags.bind(new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, path).toString());
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES = mod("quantum_singularities");

        // quantum singularities
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_COAL = mod("quantum_singularities/coal");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_COBBLESTONE = mod("quantum_singularities/cobblestone");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_DIRT = mod("quantum_singularities/dirt");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_GRAVEL = mod("quantum_singularities/gravel");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_REDSTONE = mod("quantum_singularities/redstone");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_SAND = mod("quantum_singularities/sand");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_IRON = mod("quantum_singularities/iron");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_GOLD = mod("quantum_singularities/gold");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_DIAMOND = mod("quantum_singularities/diamond");
        public static final ITag.INamedTag<Item> QUANTUM_SINGULARITIES_EMERALD = mod("quantum_singularities/emerald");

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> minecraft(String path) {
            return ItemTags.bind(new ResourceLocation("minecraft", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.bind(new ResourceLocation(CuboidQuantumSingularitiesMod.MOD_ID, path).toString());
        }
    }
}
