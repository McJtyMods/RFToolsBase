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
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;
import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;

public class TabletItem extends BaseItem implements IItemCycler, ITooltipSettings {

    public static final ManualEntry MANUAL = ManualHelper.create("rftoolsbase:tools/tablet");

    private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(() -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(), gold()));

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
        // @todo 1.21
//        stack.getOrCreateTag().putInt("Current", current);
        ItemStack containingItem = getContainingItem(stack, current);
        ItemStack newTablet = deriveNewItemstack(current, containingItem, stack, current);
        player.getInventory().items.set(player.getInventory().selected, newTablet);
//        player.setHeldItem(getHand(player), newTablet);
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
            if (!containingItem.isEmpty()) {
                setCurrentSlot(player, stack, currentItem);
                player.displayClientMessage(ComponentFactory.literal("Switched item"), false);
                return;
            }
            tries--;
        }
    }

    public static ItemStack getContainingItem(ItemStack stack, int slot) {
        // @todo 1.21
//        return NBTTools.getTag(stack).map(tag -> ItemStack.of(tag.getCompound("Item" + slot))).orElse(ItemStack.EMPTY);
        return ItemStack.EMPTY;
    }

    public static void setContainingItem(Player player, InteractionHand hand, int slot, ItemStack containingItem) {
        ItemStack stack = player.getItemInHand(hand);
        // @todo 1.21
//        CompoundTag tag = stack.getOrCreateTag();
//        if (containingItem.isEmpty()) {
//            tag.remove("Item" + slot);
//        } else {
//            CompoundTag compound = new CompoundTag();
//            containingItem.save(compound);
//            tag.put("Item" + slot, compound);
//        }

        int current = getCurrentSlot(stack);
        ItemStack newTablet = deriveNewItemstack(slot, containingItem, stack, current);
        player.getInventory().items.set(player.getInventory().selected, newTablet);
//        player.setHeldItem(hand, newTablet);
    }

    private static ItemStack deriveNewItemstack(int slot, ItemStack containingItem, ItemStack stack, int current) {
        ItemStack newTablet;
        if (slot == current) {
            if (containingItem.isEmpty()) {
                newTablet = new ItemStack(TabletModule.TABLET.get());
            } else {
                newTablet = new ItemStack(((ITabletSupport) containingItem.getItem()).getInstalledTablet());
            }
            // @todo 1.21
//            newTablet.setTag(stack.getTag());
        } else {
            newTablet = stack;
        }
        return newTablet;
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
        player.openMenu(new MenuProvider() {
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
    public void appendHoverText(@Nonnull ItemStack itemStack, TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flags) {
        super.appendHoverText(itemStack, context, list, flags);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flags);
    }
}