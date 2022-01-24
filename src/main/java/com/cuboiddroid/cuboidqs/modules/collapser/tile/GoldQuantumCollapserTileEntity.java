package com.cuboiddroid.cuboidqs.modules.collapser.tile;

import com.cuboiddroid.cuboidqs.Config;
import com.cuboiddroid.cuboidqs.modules.collapser.inventory.GoldQuantumCollapserContainer;
import com.cuboiddroid.cuboidqs.setup.ModTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class GoldQuantumCollapserTileEntity extends QuantumCollapserTileEntityBase {
    public GoldQuantumCollapserTileEntity() {
        super(ModTileEntities.GOLD_QUANTUM_COLLAPSER.get(),
                Config.goldQuantumCollapserSpeed.get().floatValue(),
                2);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.cuboidqs.gold_quantum_collapser");
    }

    @Override
    public Container createContainer(int i, World level, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new GoldQuantumCollapserContainer(i, level, pos, playerInventory, playerEntity);
    }

}