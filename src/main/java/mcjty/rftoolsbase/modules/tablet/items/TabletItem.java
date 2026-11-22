package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.gui.ManualEntry;
import mcjty.lib.items.BaseItem;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.ComponentFactory;
import mcjty.lib.varia.NBTTools;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.various.IItemCycler;
import mcjty.rftoolsbase.api.various.ITabletSupport;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.tools.ManualHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;
import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;

public class TabletItem extends BaseItem implements IItemCycler, ITooltipSettings {

    public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/tablet");

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold());

    @Override
    public ManualEntry getManualEntry() {
        return MANUAL;
    }

    public TabletItem() {
        super(RFToolsBase.setup.defaultProperties().stacksTo(1));
    }

    public static int getCurrentSlot(ItemStack stack) {
        return NBTTools.getTag(stack).map(tag -> tag.getInt("Current")).orElse(0);
    }

    public static void setCurrentSlot(Player player, ItemStack stack, int current) {
        if (!(stack.getItem() instanceof TabletItem) || current < 0 || current >= NUM_SLOTS) {
            return;
        }
        ItemStack containingItem = getContainingItem(stack, current);
        if (!containingItem.isEmpty() && !(containingItem.getItem() instanceof ITabletSupport)) {
            return;
        }
        ItemStack tablet = getTabletVariant(stack, containingItem);
        tablet.getOrCreateTag().putInt("Current", current);
        player.setItemInHand(InteractionHand.MAIN_HAND, tablet);
    }

    public static InteractionHand getHand(Player player) {
        return player.getUsedItemHand() == null ? InteractionHand.MAIN_HAND : player.getUsedItemHand();
    }

    @Override
    public List<ItemStack> getItemsForTab() {
        return Collections.singletonList(new ItemStack(TabletModule.TABLET.get()));
    }


    @Override
    public void cycle(Player player, ItemStack stack, boolean next) {
        int currentItem = getCurrentSlot(stack);
        int tries = NUM_SLOTS+1;
        while (tries > 0) {
            if (next) {
                currentItem = (currentItem + 1) % NUM_SLOTS;
            } else {
                currentItem = (currentItem + NUM_SLOTS - 1) % NUM_SLOTS;
            }
            ItemStack containingItem = getContainingItem(stack, currentItem);
            if (containingItem.getItem() instanceof ITabletSupport) {
                setCurrentSlot(player, stack, currentItem);
                player.displayClientMessage(ComponentFactory.literal("Switched item"), false);
                return;
            }
            tries--;
        }
    }

    public static ItemStack getContainingItem(ItemStack stack, int slot) {
        return NBTTools.getTag(stack).map(tag -> ItemStack.of(tag.getCompound("Item" + slot))).orElse(ItemStack.EMPTY);
    }

    public static void setContainingItem(Player player, InteractionHand hand, int slot, ItemStack containingItem) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof TabletItem) || slot < 0 || slot >= NUM_SLOTS
                || (!containingItem.isEmpty() && !(containingItem.getItem() instanceof ITabletSupport))) {
            return;
        }

        ItemStack tablet = slot == getCurrentSlot(stack) ? getTabletVariant(stack, containingItem) : stack;
        CompoundTag tag = tablet.getOrCreateTag();
        if (containingItem.isEmpty()) {
            tag.remove("Item" + slot);
        } else {
            tag.put("Item" + slot, containingItem.save(new CompoundTag()));
        }
        player.setItemInHand(hand, tablet);
    }

    private static ItemStack getTabletVariant(ItemStack tablet, ItemStack containingItem) {
        Item variant = containingItem.getItem() instanceof ITabletSupport support
                ? support.getInstalledTablet() : TabletModule.TABLET.get();
        if (tablet.is(variant)) {
            return tablet;
        }
        ItemStack replacement = new ItemStack(variant, tablet.getCount());
        if (tablet.hasTag()) {
            replacement.setTag(tablet.getTag().copy());
        }
        return replacement;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            if (player.isShiftKeyDown()) {
                openTabletGui(player);
            } else {
                ItemStack containingItem = getContainingItem(stack, getCurrentSlot(stack));
                if (containingItem.isEmpty()) {
                    openTabletGui(player);
                } else {
                    if (containingItem.getItem() instanceof ITabletSupport) {
                        ((ITabletSupport) containingItem.getItem()).openGui(player, stack, containingItem);
                    }
                }
            }

            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    private void openTabletGui(Player player) {
        NetworkHooks.openScreen((ServerPlayer)player, new MenuProvider() {
            @Nonnull
            @Override
            public Component getDisplayName() {
                return ComponentFactory.literal("Tablet");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player) {
                TabletContainer container = new TabletContainer(id, player.blockPosition(), player);
                container.setupInventories(new TabletItemHandler(player), playerInventory);
                return container;
            }
        });
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flags) {
        super.appendHoverText(itemStack, world, list, flags);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flags);
    }
}