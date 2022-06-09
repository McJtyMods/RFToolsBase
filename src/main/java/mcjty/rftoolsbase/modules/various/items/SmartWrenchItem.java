package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.api.smartwrench.ISmartWrenchSelector;
import mcjty.lib.api.smartwrench.SmartWrench;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.BlockPosTools;
import mcjty.lib.varia.LevelTools;
import mcjty.lib.varia.Logging;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousModule;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            SmartWrenchMode mode = getCurrentMode(stack);
            CompoundTag tag = stack.getTag();
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
            Logging.message(player, ChatFormatting.YELLOW + "Smart wrench is now in " + mode.getName() + " mode.");
        }
        return super.use(world, player, hand);
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            Player player = context.getPlayer();
            InteractionHand hand = context.getHand();
            ItemStack stack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();

            if (player != null && player.isShiftKeyDown()) {
                // Make sure the block get activated if it is a BaseBlockNew
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof BaseBlock) {
                    if (state.use(world, player, hand, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, context.isInside())) == InteractionResult.SUCCESS) {
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            SmartWrenchMode mode = getCurrentMode(stack);
            if (mode == SmartWrenchMode.MODE_SELECT) {
                return getCurrentBlock(stack).map(b -> {
                    if (!b.dimension().equals(world.dimension())) {
                        if (player != null) {
                            Logging.message(player, ChatFormatting.RED + "The selected block is in another dimension!");
                        }
                        return InteractionResult.FAIL;
                    }
                    BlockEntity te = world.getBlockEntity(b.pos());
                    if (te instanceof ISmartWrenchSelector smartWrenchSelector) {
                        smartWrenchSelector.selectBlock(player, pos);
                    }
                    return InteractionResult.SUCCESS;
                }).orElse(InteractionResult.SUCCESS);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flags) {
        super.appendHoverText(itemStack, world, list, flags);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flags);
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
        CompoundTag tagCompound = itemStack.getOrCreateTag();

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
        CompoundTag tagCompound = itemStack.getTag();
        if (tagCompound != null && tagCompound.contains("selectedX")) {
            int x = tagCompound.getInt("selectedX");
            int y = tagCompound.getInt("selectedY");
            int z = tagCompound.getInt("selectedZ");
            String dim = tagCompound.getString("selectedDim");
            return Optional.of(GlobalPos.of(LevelTools.getId(dim), new BlockPos(x, y, z)));
        }
        return Optional.empty();
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 1;
    }

}