package mcjty.rftoolsbase.modules.informationscreen.blocks;

import mcjty.lib.McJtyLib;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.RotationType;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.varia.OrientationTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class InformationScreenBlock extends BaseBlock {

    public InformationScreenBlock() {
        super(new BlockBuilder()
                .tileEntitySupplier(InformationScreenTileEntity::new));
    }

    @Override
    public void addInformation(ItemStack itemStack, IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        if (McJtyLib.proxy.isShiftKeyDown()) {
            list.add(new StringTextComponent(TextFormatting.WHITE + "Place this block on a powercell"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "to display information about that cell"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "Right click with a wrench to change"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "modes"));
            list.add(new StringTextComponent(TextFormatting.GRAY + "Bonus: this block can display"));
            list.add(new StringTextComponent(TextFormatting.GRAY + "power information about any block"));
            list.add(new StringTextComponent(TextFormatting.GRAY + "that supports RF/FE"));
        } else {
            list.add(new StringTextComponent(TextFormatting.WHITE + "<Press Shift>"));
        }
    }

    public static final VoxelShape BLOCK_NORTH = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 1F);
    public static final VoxelShape BLOCK_SOUTH = Block.makeCuboidShape(0.0F, 0.0F, 15F, 16.0F, 16.0F, 16.0F);
    public static final VoxelShape BLOCK_WEST = Block.makeCuboidShape(0.0F, 0.0F, 0.0F, 1F, 16.0F, 16.0F);
    public static final VoxelShape BLOCK_EAST = Block.makeCuboidShape(15F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction side = OrientationTools.getOrientationHoriz(state);
        switch (side) {
            case NORTH:
                return BLOCK_SOUTH;
            case EAST:
                return BLOCK_WEST;
            case WEST:
                return BLOCK_EAST;
            default:
                return BLOCK_NORTH;
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        boolean rc = super.onBlockActivated(state, world, pos, player, hand, result);
        if (!rc) {
            // We just pass along the block activation to the block behind it so that we can open the gui of that
            BlockPos offset = pos.offset(OrientationTools.getOrientationHoriz(state).getOpposite());
            result = new BlockRayTraceResult(result.getHitVec(), result.getFace(), offset, result.isInside());
            return world.getBlockState(offset).onBlockActivated(world, player, hand, result);
        }
        return rc;
    }

    @Override
    protected boolean openGui(World world, int x, int y, int z, PlayerEntity player) {
        // This block does not have a gui
        return false;
    }

    @Override
    protected boolean wrenchUse(World world, BlockPos pos, Direction side, PlayerEntity player) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof InformationScreenTileEntity) {
                InformationScreenTileEntity monitor = (InformationScreenTileEntity) te;
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
