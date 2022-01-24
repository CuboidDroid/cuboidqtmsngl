package com.cuboiddroid.cuboidqs.setup;

import com.cuboiddroid.cuboidqs.modules.collapser.inventory.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
    static void register() {
    }

    // quantum collapsers
    public static final RegistryObject<ContainerType<IronQuantumCollapserContainer>> IRON_QUANTUM_COLLAPSER =
            Registration.CONTAINERS.register("iron_quantum_collapser", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new IronQuantumCollapserContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<GoldQuantumCollapserContainer>> GOLD_QUANTUM_COLLAPSER =
            Registration.CONTAINERS.register("gold_quantum_collapser", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new GoldQuantumCollapserContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<DiamondQuantumCollapserContainer>> DIAMOND_QUANTUM_COLLAPSER =
            Registration.CONTAINERS.register("diamond_quantum_collapser", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new DiamondQuantumCollapserContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<EmeraldQuantumCollapserContainer>> EMERALD_QUANTUM_COLLAPSER =
            Registration.CONTAINERS.register("emerald_quantum_collapser", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new EmeraldQuantumCollapserContainer(windowId, world, pos, inv, inv.player);
            }));

    public static final RegistryObject<ContainerType<NetheriteQuantumCollapserContainer>> NETHERITE_QUANTUM_COLLAPSER =
            Registration.CONTAINERS.register("netherite_quantum_collapser", () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getCommandSenderWorld();  // possibly level instead?
                return new NetheriteQuantumCollapserContainer(windowId, world, pos, inv, inv.player);
            }));
}
