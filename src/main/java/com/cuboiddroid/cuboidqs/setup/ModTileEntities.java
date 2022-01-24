package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.modules.collapser.tile.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModTileEntities {
    public static void register() {
    }

    // Quantum collapsers

    public static final RegistryObject<TileEntityType<IronQuantumCollapserTileEntity>> IRON_QUANTUM_COLLAPSER = register(
            "iron_quantum_collapser", IronQuantumCollapserTileEntity::new, ModBlocks.IRON_QUANTUM_COLLAPSER);

    public static final RegistryObject<TileEntityType<GoldQuantumCollapserTileEntity>> GOLD_QUANTUM_COLLAPSER = register(
            "gold_quantum_collapser", GoldQuantumCollapserTileEntity::new, ModBlocks.GOLD_QUANTUM_COLLAPSER);

    public static final RegistryObject<TileEntityType<DiamondQuantumCollapserTileEntity>> DIAMOND_QUANTUM_COLLAPSER = register(
            "diamond_quantum_collapser", DiamondQuantumCollapserTileEntity::new, ModBlocks.DIAMOND_QUANTUM_COLLAPSER);

    public static final RegistryObject<TileEntityType<EmeraldQuantumCollapserTileEntity>> EMERALD_QUANTUM_COLLAPSER = register(
            "emerald_quantum_collapser", EmeraldQuantumCollapserTileEntity::new, ModBlocks.EMERALD_QUANTUM_COLLAPSER);

    public static final RegistryObject<TileEntityType<NetheriteQuantumCollapserTileEntity>> NETHERITE_QUANTUM_COLLAPSER = register(
            "netherite_quantum_collapser", NetheriteQuantumCollapserTileEntity::new, ModBlocks.NETHERITE_QUANTUM_COLLAPSER);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            //noinspection ConstantConditions - null in build
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }
}