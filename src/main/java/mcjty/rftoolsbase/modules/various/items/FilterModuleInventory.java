package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.varia.ItemStackList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class FilterModuleInventory implements IItemHandlerModifiable {

    private ItemStackList stacks = ItemStackList.create(FilterModuleContainer.FILTER_SLOTS);
    private final PlayerEntity entityPlayer;

    public FilterModuleInventory(PlayerEntity player) {
        this.entityPlayer = player;
        CompoundNBT tagCompound = entityPlayer.getHeldItem(Hand.MAIN_HAND).getOrCreateTag();
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < bufferTagList.size(); i++) {
            CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
            stacks.set(i, ItemStack.read(nbtTagCompound));
        }
    }

    @Override
    public void setStackInSlot(int index, @Nonnull ItemStack stack) {
        if (index >= stacks.size()) {
            return;
        }

        if (FilterModuleContainer.CONTAINER_FACTORY.isGhostSlot(index)) {
            if (!stack.isEmpty()) {
                ItemStack stack1 = stack.copy();
                if (index < 9) {
                    stack1.setCount(1);
                }
                stacks.set(index, stack1);
            } else {
                stacks.set(index, ItemStack.EMPTY);
            }
        } else {
            stacks.set(index, stack);
            if (!stack.isEmpty() && stack.getCount() > getSlots()) {
                int amount = getSlotLimit(index);
                if (amount <= 0) {
                    stack.setCount(0);
                } else {
                    stack.setCount(amount);
                }
            }
        }
        markDirty();
    }

    @Override
    public int getSlots() {
        return FilterModuleContainer.FILTER_SLOTS;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = getStackInSlot(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot))) {
                return stack;
            }

            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
                return stack;
            }

            if (!isItemValid(slot, stack)) {
                return stack;
            }

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    setStackInSlot(slot, copy);
                    markDirty();
                }

                return ItemStack.EMPTY;
            } else {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    setStackInSlot(slot, copy);
                    markDirty();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (!isItemValid(slot, stack)) {
                return stack;
            }

            m = Math.min(stack.getMaxStackSize(), getSlotLimit(slot));
            if (m < stack.getCount()) {
                // copy the stack to not modify the original one
                stack = stack.copy();
                if (!simulate) {
                    setStackInSlot(slot, stack.split(m));
                    markDirty();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    setStackInSlot(slot, stack);
                    markDirty();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack stackInSlot = getStackInSlot(slot);

        if (stackInSlot.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (simulate) {
            if (stackInSlot.getCount() < amount) {
                return stackInSlot.copy();
            } else {
                ItemStack copy = stackInSlot.copy();
                copy.setCount(amount);
                return copy;
            }
        } else {
            int m = Math.min(stackInSlot.getCount(), amount);

            ItemStack decrStackSize = decrStackSize(slot, m);
            markDirty();
            return decrStackSize;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }


    @Override
    public ItemStack getStackInSlot(int index) {
        return stacks.get(index);
    }

    private ItemStack decrStackSize(int index, int amount) {
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
        }
        if (!stacks.get(index).isEmpty()) {
            if (stacks.get(index).getCount() <= amount) {
                ItemStack old = stacks.get(index);
                stacks.set(index, ItemStack.EMPTY);
                markDirty();
                return old;
            }
            ItemStack its = stacks.get(index).split(amount);
            if (stacks.get(index).isEmpty()) {
                stacks.set(index, ItemStack.EMPTY);
            }
            markDirty();
            return its;
        }
        return ItemStack.EMPTY;
    }

    private void markDirty() {
        ItemStack heldItem = entityPlayer.getHeldItem(Hand.MAIN_HAND);
        if (!(heldItem).isEmpty()) {
            CompoundNBT tagCompound = heldItem.getTag();
            convertItemsToNBT(tagCompound, stacks);
        }
    }

    public static void convertItemsToNBT(CompoundNBT tagCompound, ItemStackList stacks) {
        ListNBT bufferTagList = new ListNBT();
        for (ItemStack stack : stacks) {
            CompoundNBT nbtTagCompound = new CompoundNBT();
            if (!stack.isEmpty()) {
                stack.write(nbtTagCompound);
            }
            bufferTagList.add(nbtTagCompound);
        }
        tagCompound.put("Items", bufferTagList);
    }

    private ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        setStackInSlot(index, ItemStack.EMPTY);
        return stack;
    }
}
