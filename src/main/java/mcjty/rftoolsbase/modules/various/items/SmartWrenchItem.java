package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.api.smartwrench.ISmartWrenchSelector;
import mcjty.lib.api.smartwrench.SmartWrench;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.GlobalCoordinate;
import mcjty.lib.varia.Logging;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousSetup;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static mcjty.lib.builder.TooltipBuilder.*;

public class SmartWrenchItem extends Item implements SmartWrench, ITooltipSettings {

    private final SmartWrenchMode mode;

    private final TooltipBuilder tooltipBuilder = new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold(),
                    parameter("info1", stack -> getMode().getName()),
                    parameter("info2", stack -> getCurrentBlock(stack).map(GlobalCoordinate::toString).orElse("<not selected>")));

    public SmartWrenchItem(SmartWrenchMode mode) {
        super(new Properties()
                .maxStackSize(1)
                .group(RFToolsBase.setup.getTab()));
        this.mode = mode;
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
                newStack = new ItemStack(VariousSetup.SMARTWRENCH_SELECT.get());
            } else {
                mode = SmartWrenchMode.MODE_WRENCH;
                newStack = new ItemStack(VariousSetup.SMARTWRENCH.get());
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
                    if (state.onBlockActivated(world, player, hand, new BlockRayTraceResult(context.getHitVec(), context.getFace(), pos, context.isInside())) == ActionResultType.SUCCESS) {
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            SmartWrenchMode mode = getCurrentMode(stack);
            if (mode == SmartWrenchMode.MODE_SELECT) {
                return getCurrentBlock(stack).map(b -> {
                    if (!b.getDimension().equals(world.getDimension().getType())) {
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
                    return ActionResultType.SUCCESS;
                }).orElse(ActionResultType.SUCCESS);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        super.addInformation(itemStack, world, list, flags);
        tooltipBuilder.makeTooltip(getRegistryName(), itemStack, list, flags);
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
            tagCompound.putString("selectedDim", c.getDimension().getRegistryName().toString());
        }
    }

    @Nonnull
    public static Optional<GlobalCoordinate> getCurrentBlock(ItemStack itemStack) {
        CompoundNBT tagCompound = itemStack.getTag();
        if (tagCompound != null && tagCompound.contains("selectedX")) {
            int x = tagCompound.getInt("selectedX");
            int y = tagCompound.getInt("selectedY");
            int z = tagCompound.getInt("selectedZ");
            String dim = tagCompound.getString("selectedDim");
            return Optional.of(new GlobalCoordinate(new BlockPos(x, y, z), DimensionType.byName(new ResourceLocation(dim))));
        }
        return Optional.empty();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

}