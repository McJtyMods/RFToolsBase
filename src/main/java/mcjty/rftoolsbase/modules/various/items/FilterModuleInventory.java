package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.varia.ItemStackList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.Constants;

public class FilterModuleInventory implements IInventory
{
    private ItemStackList stacks = ItemStackList.create(FilterModuleContainer.FILTER_SLOTS);
    private final PlayerEntity entityPlayer;

    public FilterModuleInventory(PlayerEntity player) {
        this.entityPlayer = player;
        CompoundNBT tagCompound = entityPlayer.getHeldItem(Hand.MAIN_HAND).getOrCreateTag();
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < bufferTagList.size() ; i++) {
            CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
            stacks.set(i, ItemStack.read(nbtTagCompound));
        }
    }

    @Override
    public int getSizeInventory() {
        return FilterModuleContainer.FILTER_SLOTS;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return stacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
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

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
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
            if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
                int amount = getInventoryStackLimit();
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
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

    public boolean isUsable(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        setInventorySlotContents(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return isUsable(player);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
