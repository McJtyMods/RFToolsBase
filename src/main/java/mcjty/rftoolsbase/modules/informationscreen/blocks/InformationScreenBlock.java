package mcjty.rftoolsbase.modules.informationscreen.blocks;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RotationType;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.varia.OrientationTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

import static mcjty.lib.builder.TooltipBuilder.*;

public class InformationScreenBlock extends BaseBlock {

    public InformationScreenBlock() {
        super(new BlockBuilder()
                .info(key("message.rftoolsbase.shiftmessage"))
                .infoShift(header(), gold())
                .tileEntitySupplier(InformationScreenTileEntity::new));
    }

    public static final VoxelShape BLOCK_NORTH = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 1F);
    public static final VoxelShape BLOCK_SOUTH = Block.box(0.0F, 0.0F, 15F, 16.0F, 16.0F, 16.0F);
    public static final VoxelShape BLOCK_WEST = Block.box(0.0F, 0.0F, 0.0F, 1F, 16.0F, 16.0F);
    public static final VoxelShape BLOCK_EAST = Block.box(15F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        Direction side = OrientationTools.getOrientationHoriz(state);
        return switch (side) {
            case NORTH -> BLOCK_SOUTH;
            case EAST -> BLOCK_WEST;
            case WEST -> BLOCK_EAST;
            default -> BLOCK_NORTH;
        };
    }

    @Nonnull
    @Override
    public InteractionResult useWithoutItem(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult result) {
        InteractionResult rc = super.useWithoutItem(state, world, pos, player, result);
        if (rc != InteractionResult.SUCCESS) {
            // We just pass along the block activation to the block behind it so that we can open the gui of that
            BlockPos offset = pos.relative(OrientationTools.getOrientationHoriz(state).getOpposite());
            result = new BlockHitResult(result.getLocation(), result.getDirection(), offset, result.isInside());
            return world.getBlockState(offset).useWithoutItem(world, player, result);
        }
        return rc;
    }

    @Override
    protected boolean openGui(Level world, int x, int y, int z, Player player) {
        // This block does not have a gui
        return false;
    }

    @Override
    protected boolean wrenchUse(Level world, BlockPos pos, Direction side, Player player) {
        if (!world.isClientSide) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof InformationScreenTileEntity monitor) {
                monitor.toggleMode();
            }
        }
        return true;
    }



    @Override
    public RotationType getRotationType() {
        return RotationType.HORIZROTATION;
    }


    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }
}
