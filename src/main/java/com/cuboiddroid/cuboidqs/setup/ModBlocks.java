package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.CuboidQuantumSingularitiesMod;
import com.cuboiddroid.cuboidqs.modules.collapser.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
  // Quantum Collapsers

  public static final RegistryObject<IronQuantumCollapserBlock> IRON_QUANTUM_COLLAPSER = register(
          "iron_quantum_collapser", () ->
                  new IronQuantumCollapserBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<GoldQuantumCollapserBlock> GOLD_QUANTUM_COLLAPSER = register(
          "gold_quantum_collapser", () ->
                  new GoldQuantumCollapserBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<DiamondQuantumCollapserBlock> DIAMOND_QUANTUM_COLLAPSER = register(
          "diamond_quantum_collapser", () ->
                  new DiamondQuantumCollapserBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<EmeraldQuantumCollapserBlock> EMERALD_QUANTUM_COLLAPSER = register(
          "emerald_quantum_collapser", () ->
                  new EmeraldQuantumCollapserBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  public static final RegistryObject<NetheriteQuantumCollapserBlock> NETHERITE_QUANTUM_COLLAPSER = register(
          "netherite_quantum_collapser", () ->
                  new NetheriteQuantumCollapserBlock(AbstractBlock.Properties.of(Material.METAL)
                          .strength(4, 20)
                          .sound(SoundType.METAL)));

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  static void register() {
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> blockSupplier) {
    RegistryObject<T> ret = registerNoItem(name, blockSupplier);
    Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(CuboidQuantumSingularitiesMod.CUBOIDQS_ITEM_GROUP)));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
    return register(name, sup, block -> item(block, renderMethod));
  }

  private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
    RegistryObject<T> ret = registerNoItem(name, sup);
    Registration.ITEMS.register(name, itemCreator.apply(ret));
    return ret;
  }

  private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> blockSupplier) {
    return Registration.BLOCKS.register(name, blockSupplier);
  }

  private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
    return () -> new BlockItem(block.get(), new Item.Properties().tab(CuboidQuantumSingularitiesMod.CUBOIDQS_ITEM_GROUP).setISTER(renderMethod));
  }
}
