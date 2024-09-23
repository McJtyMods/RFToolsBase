package mcjty.rftoolsbase.modules.filter.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.tooltips.ITooltipExtras;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.InventoryTools;
import mcjty.lib.varia.TagTools;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.FilterModuleCache;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mcjty.lib.builder.TooltipBuilder.*;

public class FilterModuleItem extends Item implements ITooltipSettings, ITooltipExtras {

    public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/filtermodule");
    private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(() -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold(),
                    parameter("info", stack -> {
                        FilterModuleData data = stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
                        /// @todo 1.21
//                        CompoundTag tagCompound = stack.getTag();
//                        if (tagCompound != null) {
//                            String blackListMode = tagCompound.getString("blacklistMode");
//                            String modeLine = "Mode " + ("Black".equals(blackListMode) ? "blacklist" : "whitelist");
//                            if (tagCompound.getBoolean("damageMode")) {
//                                modeLine += ", Damage";
//                            }
//                            if (tagCompound.getBoolean("nbtMode")) {
//                                modeLine += ", NBT";
//                            }
//                            if (tagCompound.getBoolean("modMode")) {
//                                modeLine += ", Mod";
//                            }
//                            return modeLine;
//                        }
                        return "<not configured>";
                    })));


    public FilterModuleItem() {
        super(RFToolsBase.setup.defaultProperties().stacksTo(1));
    }

    @Override
    public ManualEntry getManualEntry() {
        return MANUAL;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(itemStack, context, list, flagIn);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flagIn);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level world = context.getLevel();
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = context.getClickedPos();
        if (player.isCrouching()) {
            if (!world.isClientSide) {
                BlockEntity te = world.getBlockEntity(pos);
                if (InventoryTools.isInventory(te)) {
                    FilterModuleInventory inventory = new FilterModuleInventory(stack);
                    InventoryTools.getItems(te, s -> true).forEach(inventory::addStack);
                    inventory.markDirty();
                    player.displayClientMessage(ComponentFactory.literal(ChatFormatting.GREEN + "Stored inventory contents in filter"), false);
                } else {
                    BlockState state = world.getBlockState(pos);
                    ItemStack blockStack = state.getBlock().getCloneItemStack(world, pos, state);
                    if (!blockStack.isEmpty()) {
                        FilterModuleInventory inventory = new FilterModuleInventory(stack);
                        inventory.addStack(blockStack);
                        inventory.markDirty();
                        player.displayClientMessage(ComponentFactory.literal(ChatFormatting.GREEN + "Added " + blockStack.getHoverName().getString() /* was getFormattedText() */ + " to the filter!"), false);
                    } else {
                        player.displayClientMessage(ComponentFactory.literal(ChatFormatting.RED + "Could not add " + blockStack.getHoverName().getString() /* was getFormattedText() */ + " to the filter!"), false);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            player.openMenu(new MenuProvider() {
                @Nonnull
                @Override
                public Component getDisplayName() {
                    return ComponentFactory.literal("Filter Module");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player) {
                    FilterModuleContainer container = new FilterModuleContainer(id, player.blockPosition(), player);
                    container.setupInventories(null, playerInventory);
                    return container;
                }
            });

            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public static Predicate<ItemStack> getCache(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return new FilterModuleCache(stack);
    }

    @Override
    public List<Pair<ItemStack, Integer>> getItems(ItemStack stack) {
        FilterModuleInventory inventory = new FilterModuleInventory(stack);
        Set<Item> itemSet = new HashSet<>();
        for (ItemStack s : inventory.getStacks()) {
            itemSet.add(s.getItem());
        }
        for (TagKey<Item> tag : inventory.getTags()) {
            TagTools.getItemsForTag(tag).forEach(i -> itemSet.add(i.value()));
        }
        return itemSet.stream().map(item -> Pair.of(new ItemStack(item), ITooltipExtras.NOAMOUNT)).collect(Collectors.toList());
    }
}
