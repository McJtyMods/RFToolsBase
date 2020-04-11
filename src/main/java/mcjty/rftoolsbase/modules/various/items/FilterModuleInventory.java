package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.varia.ItemStackList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.function.Supplier;

public class FilterModuleInventory {

    private ItemStackList stacks = ItemStackList.create();
    private final Supplier<ItemStack> filterGetter;

    public FilterModuleInventory(PlayerEntity player) {
        filterGetter = () -> player.getHeldItem(Hand.MAIN_HAND);
        CompoundNBT tagCompound = player.getHeldItem(Hand.MAIN_HAND).getOrCreateTag();
        convertFromNBT(tagCompound);
    }

    public FilterModuleInventory(ItemStack filterItem) {
        filterGetter = () -> filterItem;
        CompoundNBT tagCompound = filterItem.getOrCreateTag();
        convertFromNBT(tagCompound);
    }

    private void convertFromNBT(CompoundNBT tagCompound) {
        ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < bufferTagList.size(); i++) {
            CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
            ItemStack s = ItemStack.read(nbtTagCompound);
            if (!s.isEmpty()) {
                stacks.add(s);
            }
        }
    }

    public void addStack(ItemStack stack) {
        ItemStack toPlace = stack.copy();
        toPlace.setCount(1);
        for (ItemStack s : stacks) {
            if (ItemHandlerHelper.canItemStacksStack(s, toPlace)) {
                return;
            }
        }

        stacks.add(toPlace);
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    public void markDirty() {
        ItemStack heldItem = filterGetter.get();
        if (!heldItem.isEmpty()) {
            CompoundNBT tagCompound = heldItem.getOrCreateTag();
            convertItemsToNBT(tagCompound, stacks);
        }
    }

    public static void convertItemsToNBT(CompoundNBT tagCompound, List<ItemStack> stacks) {
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
}
