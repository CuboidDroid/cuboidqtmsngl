package com.cuboiddroid.cuboidqs.modules.collapser.block;

import com.cuboiddroid.cuboidqs.modules.collapser.tile.EmeraldQuantumCollapserTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class EmeraldQuantumCollapserBlock extends QuantumCollapserBlockBase {

    public EmeraldQuantumCollapserBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new EmeraldQuantumCollapserTileEntity();
    }
}