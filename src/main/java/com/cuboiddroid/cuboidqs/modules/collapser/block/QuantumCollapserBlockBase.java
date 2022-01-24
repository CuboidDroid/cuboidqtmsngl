package com.cuboiddroid.cuboidqs.modules.collapser.block;

import com.cuboiddroid.cuboidqs.modules.collapser.tile.QuantumCollapserTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public abstract class QuantumCollapserBlockBase extends Block {

    private static final VoxelShape VOXEL_SHAPE_N = Stream.of(
            Block.box(0, 0, 0, 16, 1, 16), Block.box(2, 1, 2, 14, 15, 14), Block.box(1, 15, 1, 15, 16, 15),
            Block.box(0, 1, 0, 3, 2, 3), Block.box(0, 1, 13, 3, 2, 16), Block.box(13, 1, 0, 16, 2, 3),
            Block.box(13, 1, 13, 16, 2, 16), Block.box(0, 3, 0, 1, 13, 1), Block.box(0, 14, 0, 3, 15, 3),
            Block.box(0, 2, 0, 2, 3, 2), Block.box(0, 13, 0, 2, 14, 2), Block.box(0, 2, 14, 2, 3, 16),
            Block.box(14, 2, 14, 16, 3, 16), Block.box(14, 2, 0, 16, 3, 2), Block.box(13, 14, 0, 16, 15, 3),
            Block.box(14, 13, 0, 16, 14, 2), Block.box(15, 3, 0, 16, 13, 1), Block.box(13, 14, 13, 16, 15, 16),
            Block.box(14, 13, 14, 16, 14, 16), Block.box(0, 14, 13, 3, 15, 16), Block.box(0, 13, 14, 2, 14, 16),
            Block.box(0, 3, 15, 1, 13, 16), Block.box(15, 3, 15, 16, 13, 16), Block.box(3, 3, 1, 4, 13, 2),
            Block.box(12, 3, 1, 13, 13, 2), Block.box(14, 1, 3, 15, 15, 13), Block.box(1, 1, 3, 2, 15, 13),
            Block.box(4, 3, 1, 12, 4, 2), Block.box(4, 12, 1, 12, 13, 2), Block.box(3, 1, 14, 13, 15, 15),
            Block.box(2, 15, 0, 14, 16, 1), Block.box(2, 15, 15, 14, 16, 16), Block.box(0, 15, 2, 1, 16, 14),
            Block.box(15, 15, 2, 16, 16, 14), Block.box(4, 4, 1, 12, 12, 2)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape VOXEL_SHAPE_E = Stream.of(
            Block.box(0, 0, 0, 16, 1, 16), Block.box(2, 1, 2, 14, 15, 14), Block.box(1, 15, 1, 15, 16, 15),
            Block.box(13, 1, 0, 16, 2, 3), Block.box(0, 1, 0, 3, 2, 3), Block.box(13, 1, 13, 16, 2, 16),
            Block.box(0, 1, 13, 3, 2, 16), Block.box(15, 3, 0, 16, 13, 1), Block.box(13, 14, 0, 16, 15, 3),
            Block.box(14, 2, 0, 16, 3, 2), Block.box(14, 13, 0, 16, 14, 2), Block.box(0, 2, 0, 2, 3, 2),
            Block.box(0, 2, 14, 2, 3, 16), Block.box(14, 2, 14, 16, 3, 16), Block.box(13, 14, 13, 16, 15, 16),
            Block.box(14, 13, 14, 16, 14, 16), Block.box(15, 3, 15, 16, 13, 16), Block.box(0, 14, 13, 3, 15, 16),
            Block.box(0, 13, 14, 2, 14, 16), Block.box(0, 14, 0, 3, 15, 3), Block.box(0, 13, 0, 2, 14, 2),
            Block.box(0, 3, 0, 1, 13, 1), Block.box(0, 3, 15, 1, 13, 16), Block.box(14, 3, 3, 15, 13, 4),
            Block.box(14, 3, 12, 15, 13, 13), Block.box(3, 1, 14, 13, 15, 15), Block.box(3, 1, 1, 13, 15, 2),
            Block.box(14, 3, 4, 15, 4, 12), Block.box(14, 12, 4, 15, 13, 12), Block.box(1, 1, 3, 2, 15, 13),
            Block.box(15, 15, 2, 16, 16, 14), Block.box(0, 15, 2, 1, 16, 14), Block.box(2, 15, 0, 14, 16, 1),
            Block.box(2, 15, 15, 14, 16, 16)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape VOXEL_SHAPE_S = Stream.of(
            Block.box(0, 0, 0, 16, 1, 16), Block.box(2, 1, 2, 14, 15, 14), Block.box(1, 15, 1, 15, 16, 15),
            Block.box(13, 1, 13, 16, 2, 16), Block.box(13, 1, 0, 16, 2, 3), Block.box(0, 1, 13, 3, 2, 16),
            Block.box(0, 1, 0, 3, 2, 3), Block.box(15, 3, 15, 16, 13, 16), Block.box(13, 14, 13, 16, 15, 16),
            Block.box(14, 2, 14, 16, 3, 16), Block.box(14, 13, 14, 16, 14, 16), Block.box(14, 2, 0, 16, 3, 2),
            Block.box(0, 2, 0, 2, 3, 2), Block.box(0, 2, 14, 2, 3, 16), Block.box(0, 14, 13, 3, 15, 16),
            Block.box(0, 13, 14, 2, 14, 16), Block.box(0, 3, 15, 1, 13, 16), Block.box(0, 14, 0, 3, 15, 3),
            Block.box(0, 13, 0, 2, 14, 2), Block.box(13, 14, 0, 16, 15, 3), Block.box(14, 13, 0, 16, 14, 2),
            Block.box(15, 3, 0, 16, 13, 1), Block.box(0, 3, 0, 1, 13, 1), Block.box(12, 3, 14, 13, 13, 15),
            Block.box(3, 3, 14, 4, 13, 15), Block.box(1, 1, 3, 2, 15, 13), Block.box(14, 1, 3, 15, 15, 13),
            Block.box(4, 3, 14, 12, 4, 15), Block.box(4, 12, 14, 12, 13, 15), Block.box(3, 1, 1, 13, 15, 2),
            Block.box(2, 15, 15, 14, 16, 16), Block.box(2, 15, 0, 14, 16, 1), Block.box(15, 15, 2, 16, 16, 14),
            Block.box(0, 15, 2, 1, 16, 14)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape VOXEL_SHAPE_W = Stream.of(
            Block.box(0, 0, 0, 16, 1, 16), Block.box(2, 1, 2, 14, 15, 14), Block.box(1, 15, 1, 15, 16, 15),
            Block.box(0, 1, 13, 3, 2, 16), Block.box(13, 1, 13, 16, 2, 16), Block.box(0, 1, 0, 3, 2, 3),
            Block.box(13, 1, 0, 16, 2, 3), Block.box(0, 3, 15, 1, 13, 16), Block.box(0, 14, 13, 3, 15, 16),
            Block.box(0, 2, 14, 2, 3, 16), Block.box(0, 13, 14, 2, 14, 16), Block.box(14, 2, 14, 16, 3, 16),
            Block.box(14, 2, 0, 16, 3, 2), Block.box(0, 2, 0, 2, 3, 2), Block.box(0, 14, 0, 3, 15, 3),
            Block.box(0, 13, 0, 2, 14, 2), Block.box(0, 3, 0, 1, 13, 1), Block.box(13, 14, 0, 16, 15, 3),
            Block.box(14, 13, 0, 16, 14, 2), Block.box(13, 14, 13, 16, 15, 16), Block.box(14, 13, 14, 16, 14, 16),
            Block.box(15, 3, 15, 16, 13, 16), Block.box(15, 3, 0, 16, 13, 1), Block.box(1, 3, 12, 2, 13, 13),
            Block.box(1, 3, 3, 2, 13, 4), Block.box(3, 1, 1, 13, 15, 2), Block.box(3, 1, 14, 13, 15, 15),
            Block.box(1, 3, 4, 2, 4, 12), Block.box(1, 12, 4, 2, 13, 12), Block.box(14, 1, 3, 15, 15, 13),
            Block.box(0, 15, 2, 1, 16, 14), Block.box(15, 15, 2, 16, 16, 14), Block.box(2, 15, 15, 14, 16, 16),
            Block.box(2, 15, 0, 14, 16, 1)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    public QuantumCollapserBlockBase(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.LIT, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST:
                return VOXEL_SHAPE_E;
            case SOUTH:
                return VOXEL_SHAPE_S;
            case WEST:
                return VOXEL_SHAPE_W;
            default:
                return VOXEL_SHAPE_N;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        if (level.isClientSide) {
            // return success on client so player swings their hand
            return ActionResultType.SUCCESS;
        }

        this.interactWith(level, pos, player);
        return ActionResultType.CONSUME;
    }

    private void interactWith(World level, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof QuantumCollapserTileEntityBase) {
            QuantumCollapserTileEntityBase collapserTileEntity = (QuantumCollapserTileEntityBase) tileEntity;
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return collapserTileEntity.getDisplayName();
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return collapserTileEntity.createContainer(i, level, pos, playerInventory, playerEntity);
                }
            };

            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.LIT, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
        // check we're removing the right block
        if (!state.is(newState.getBlock())) {
            TileEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof IInventory) {
                InventoryHelper.dropContents(level, pos, (IInventory) tileEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
        builder.add(BlockStateProperties.LIT);
    }
}
