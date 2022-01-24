package com.cuboiddroid.cuboidqs.modules.collapser.tile;

import com.cuboiddroid.cuboidqs.Config;
import com.cuboiddroid.cuboidqs.modules.collapser.inventory.EmeraldQuantumCollapserContainer;
import com.cuboiddroid.cuboidqs.setup.ModTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class EmeraldQuantumCollapserTileEntity extends QuantumCollapserTileEntityBase {
    public EmeraldQuantumCollapserTileEntity() {
        super(ModTileEntities.EMERALD_QUANTUM_COLLAPSER.get(),
                Config.emeraldQuantumCollapserSpeed.get().floatValue(),
                4);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuboidqs.emerald_quantum_collapser");
    }

    @Override
    public Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new EmeraldQuantumCollapserContainer(i, level, pos, playerInventory, playerEntity);
    }

}