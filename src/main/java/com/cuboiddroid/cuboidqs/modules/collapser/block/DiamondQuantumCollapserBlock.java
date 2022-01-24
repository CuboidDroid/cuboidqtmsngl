package com.cuboiddroid.cuboidqs.modules.collapser.block;

import com.cuboiddroid.cuboidqs.modules.collapser.tile.DiamondQuantumCollapserTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class DiamondQuantumCollapserBlock extends QuantumCollapserBlockBase {

    public DiamondQuantumCollapserBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DiamondQuantumCollapserTileEntity();
    }
}