package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.api.smartwrench.ISmartWrenchSelector;
import mcjty.lib.api.smartwrench.SmartWrench;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.BlockPosTools;
import mcjty.lib.varia.Logging;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static mcjty.lib.builder.TooltipBuilder.*;

public class SmartWrenchItem extends Item implements SmartWrench, ITooltipSettings {

    private final SmartWrenchMode mode;

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold(),
                    parameter("info1", stack -> getMode().getName()),
                    parameter("info2", stack -> getCurrentBlock(stack).map(BlockPosTools::toString).orElse("<not selected>")));

    public SmartWrenchItem(SmartWrenchMode mode) {
        super(new Properties()
                .stacksTo(1)
                .tab(RFToolsBase.setup.getTab()));
        this.mode = mode;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            SmartWrenchMode mode = getCurrentMode(stack);
            CompoundNBT tag = stack.getTag();
            ItemStack newStack;
            if (mode == SmartWrenchMode.MODE_WRENCH) {
                mode = SmartWrenchMode.MODE_SELECT;
                newStack = new ItemStack(VariousModule.SMARTWRENCH_SELECT.get());
            } else {
                mode = SmartWrenchMode.MODE_WRENCH;
                newStack = new ItemStack(VariousModule.SMARTWRENCH.get());
            }
            newStack.setTag(tag);
            player.setItemInHand(hand, newStack);
            Logging.message(player, TextFormatting.YELLOW + "Smart wrench is now in " + mode.getName() + " mode.");
        }
        return super.use(world, player, hand);
    }

    @Override
    @Nonnull
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (!world.isClientSide) {
            PlayerEntity player = context.getPlayer();
            Hand hand = context.getHand();
            ItemStack stack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();

            if (player != null && player.isShiftKeyDown()) {
                // Make sure the block get activated if it is a BaseBlockNew
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof BaseBlock) {
                    if (state.use(world, player, hand, new BlockRayTraceResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside())) == ActionResultType.SUCCESS) {
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            SmartWrenchMode mode = getCurrentMode(stack);
            if (mode == SmartWrenchMode.MODE_SELECT) {
                return getCurrentBlock(stack).map(b -> {
                    if (!b.dimension().equals(world.dimension())) {
                        if (player != null) {
                            Logging.message(player, TextFormatting.RED + "The selected block is in another dimension!");
                        }
                        return ActionResultType.FAIL;
                    }
                    TileEntity te = world.getBlockEntity(b.pos());
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
    public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        super.appendHoverText(itemStack, world, list, flags);
        tooltipBuilder.get().makeTooltip(getRegistryName(), itemStack, list, flags);
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

    public static void setCurrentBlock(ItemStack itemStack, GlobalPos c) {
        CompoundNBT tagCompound = itemStack.getOrCreateTag();

        if (c == null) {
            tagCompound.remove("selectedX");
            tagCompound.remove("selectedY");
            tagCompound.remove("selectedZ");
            tagCompound.remove("selectedDim");
        } else {
            tagCompound.putInt("selectedX", c.pos().getX());
            tagCompound.putInt("selectedY", c.pos().getY());
            tagCompound.putInt("selectedZ", c.pos().getZ());
            tagCompound.putString("selectedDim", c.dimension().location().toString());
        }
    }

    @Nonnull
    public static Optional<GlobalPos> getCurrentBlock(ItemStack itemStack) {
        CompoundNBT tagCompound = itemStack.getTag();
        if (tagCompound != null && tagCompound.contains("selectedX")) {
            int x = tagCompound.getInt("selectedX");
            int y = tagCompound.getInt("selectedY");
            int z = tagCompound.getInt("selectedZ");
            String dim = tagCompound.getString("selectedDim");
            return Optional.of(GlobalPos.of(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim)), new BlockPos(x, y, z)));
        }
        return Optional.empty();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

}