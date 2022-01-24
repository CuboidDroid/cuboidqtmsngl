package com.cuboiddroid.cuboidqs.modules.collapser.inventory;

import com.cuboiddroid.cuboidqs.setup.ModBlocks;
import com.cuboiddroid.cuboidqs.setup.ModContainers;
import com.cuboiddroid.cuboidqs.util.ContainerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetheriteQuantumCollapserContainer extends QuantumCollapserContainerBase {

    public NetheriteQuantumCollapserContainer(int windowId, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.NETHERITE_QUANTUM_COLLAPSER.get(), windowId, level, pos, playerInventory, player);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return ContainerHelper.isWithinUsableDistance(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, ModBlocks.NETHERITE_QUANTUM_COLLAPSER.get());
    }
}