package mcjty.rftoolsbase.modules.tablet.items;

import mcjty.rftoolsbase.api.various.ITabletSupport;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

import static mcjty.rftoolsbase.modules.tablet.items.TabletContainer.NUM_SLOTS;

public class TabletItemHandler implements IItemHandlerModifiable {

    private final Player player;

    public TabletItemHandler(Player player) {
        this.player = player;
    }

    private ItemStack getTablet() {
        return player.getItemInHand(InteractionHand.MAIN_HAND);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        TabletItem.setContainingItem(player, InteractionHand.MAIN_HAND, slot, stack);
    }

    @Override
    public int getSlots() {
        return NUM_SLOTS;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return TabletItem.getContainingItem(getTablet(), slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!(stack.getItem() instanceof ITabletSupport)) {
            return stack;
        }
        if (!getStackInSlot(slot).isEmpty()) {
            return stack;
        }
        if (!simulate) {
            setStackInSlot(slot, stack);
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (simulate) {
            return getStackInSlot(slot);
        }
        ItemStack stack = getStackInSlot(slot);
        setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof ITabletSupport;
    }
}
