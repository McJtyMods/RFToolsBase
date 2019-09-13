package mcjty.rftoolsbase.items;

import mcjty.lib.api.smartwrench.SmartWrench;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.api.smartwrench.ISmartWrenchSelector;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.varia.BlockPosTools;
import mcjty.lib.varia.GlobalCoordinate;
import mcjty.lib.varia.Logging;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class SmartWrenchItem extends Item implements SmartWrench {

    private final SmartWrenchMode mode;

    public SmartWrenchItem(SmartWrenchMode mode) {
        super(new Properties()
                .maxStackSize(1)
                .group(RFToolsBase.setup.getTab()));
        this.mode = mode;
        setRegistryName(mode == SmartWrenchMode.MODE_WRENCH ? "smartwrench" : "smartwrench_select");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            SmartWrenchMode mode = getCurrentMode(stack);
            CompoundNBT tag = stack.getTag();
            ItemStack newStack;
            if (mode == SmartWrenchMode.MODE_WRENCH) {
                mode = SmartWrenchMode.MODE_SELECT;
                newStack = new ItemStack(ModItems.SMARTWRENCH_SELECT);
            } else {
                mode = SmartWrenchMode.MODE_WRENCH;
                newStack = new ItemStack(ModItems.SMARTWRENCH);
            }
            newStack.setTag(tag);
            player.setHeldItem(hand, newStack);
            Logging.message(player, TextFormatting.YELLOW + "Smart wrench is now in " + mode.getName() + " mode.");
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    @Nonnull
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            PlayerEntity player = context.getPlayer();
            Hand hand = context.getHand();
            ItemStack stack = context.getItem();
            BlockPos pos = context.getPos();

            if (player != null && player.isSneaking()) {
                // Make sure the block get activated if it is a BaseBlockNew
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof BaseBlock) {
                    if (state.onBlockActivated(world, player, hand, new BlockRayTraceResult(context.getHitVec(), context.getFace(), pos, context.func_221533_k()))) {
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            SmartWrenchMode mode = getCurrentMode(stack);
            if (mode == SmartWrenchMode.MODE_SELECT) {
                GlobalCoordinate b = getCurrentBlock(stack);
                if (b != null) {
                    if (b.getDimension() != world.getDimension().getType().getId()) {
                        if (player != null) {
                            Logging.message(player, TextFormatting.RED + "The selected block is in another dimension!");
                        }
                        return ActionResultType.FAIL;
                    }
                    TileEntity te = world.getTileEntity(b.getCoordinate());
                    if (te instanceof ISmartWrenchSelector) {
                        ISmartWrenchSelector smartWrenchSelector = (ISmartWrenchSelector) te;
                        smartWrenchSelector.selectBlock(player, pos);
                    }
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        super.addInformation(itemStack, world, list, flags);
        GlobalCoordinate b = getCurrentBlock(itemStack);
        if (b != null) {
            list.add(new StringTextComponent(TextFormatting.GREEN + "Block: " + BlockPosTools.toString(b.getCoordinate()) + " at dimension " + b.getDimension()));
        }
        SmartWrenchMode mode = getCurrentMode(itemStack);
        list.add(new StringTextComponent(TextFormatting.WHITE + "Right-click on air to change mode."));
        list.add(new StringTextComponent(TextFormatting.GREEN + "Mode: " + mode.getName()));
        if (mode == SmartWrenchMode.MODE_WRENCH) {
            list.add(new StringTextComponent(TextFormatting.WHITE + "Use as a normal wrench:"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "    Sneak-right-click to pick up machines."));
            list.add(new StringTextComponent(TextFormatting.WHITE + "    Right-click to rotate machines."));
        } else if (mode == SmartWrenchMode.MODE_SELECT) {
            list.add(new StringTextComponent(TextFormatting.WHITE + "Use as a block selector:"));
            list.add(new StringTextComponent(TextFormatting.WHITE + "    Sneak-right-click select master block."));
            list.add(new StringTextComponent(TextFormatting.WHITE + "    Right-click to associate blocks with master."));
        }
    }

    public SmartWrenchMode getMode() {
        return mode;
    }

    @Override
    public SmartWrenchMode getMode(ItemStack itemStack) {
        return getCurrentMode(itemStack);
    }

    public static SmartWrenchMode getCurrentMode(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SmartWrenchItem) {
            return ((SmartWrenchItem) itemStack.getItem()).getMode();
        }
        return SmartWrenchMode.MODE_WRENCH;
    }

    public static void setCurrentBlock(ItemStack itemStack, GlobalCoordinate c) {
        CompoundNBT tagCompound = itemStack.getOrCreateTag();

        if (c == null) {
            tagCompound.remove("selectedX");
            tagCompound.remove("selectedY");
            tagCompound.remove("selectedZ");
            tagCompound.remove("selectedDim");
        } else {
            tagCompound.putInt("selectedX", c.getCoordinate().getX());
            tagCompound.putInt("selectedY", c.getCoordinate().getY());
            tagCompound.putInt("selectedZ", c.getCoordinate().getZ());
            tagCompound.putInt("selectedDim", c.getDimension());
        }
    }

    public static GlobalCoordinate getCurrentBlock(ItemStack itemStack) {
        CompoundNBT tagCompound = itemStack.getTag();
        if (tagCompound != null && tagCompound.contains("selectedX")) {
            int x = tagCompound.getInt("selectedX");
            int y = tagCompound.getInt("selectedY");
            int z = tagCompound.getInt("selectedZ");
            int dim = tagCompound.getInt("selectedDim");
            return new GlobalCoordinate(new BlockPos(x, y, z), dim);
        }
        return null;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

}