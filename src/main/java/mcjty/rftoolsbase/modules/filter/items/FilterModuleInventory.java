package mcjty.rftoolsbase.modules.filter.items;

import mcjty.lib.varia.ItemStackList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class FilterModuleInventory {

    private ItemStackList stacks = ItemStackList.create();
    private Set<ResourceLocation> tags = new HashSet<>();

    private final Supplier<ItemStack> filterGetter;

    public FilterModuleInventory(PlayerEntity player) {
        filterGetter = () -> player.getItemInHand(Hand.MAIN_HAND);
        CompoundNBT tagCompound = player.getItemInHand(Hand.MAIN_HAND).getOrCreateTag();
        convertFromNBT(tagCompound);
    }

    public FilterModuleInventory(ItemStack filterItem) {
        filterGetter = () -> filterItem;
        CompoundNBT tagCompound = filterItem.getOrCreateTag();
        convertFromNBT(tagCompound);
    }

    private void convertFromNBT(CompoundNBT tagCompound) {
        ListNBT itemList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundNBT compound = itemList.getCompound(i);
            ItemStack s = ItemStack.of(compound);
            if (!s.isEmpty()) {
                stacks.add(s);
            }
        }
        ListNBT tagList = tagCompound.getList("Tags", Constants.NBT.TAG_STRING);
        for (int i = 0 ; i < tagList.size() ; i++) {
            String s = tagList.getString(i);
            tags.add(new ResourceLocation(s));
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

    public void removeStack(ItemStack stack) {
        for (int i = 0 ; i < stacks.size() ; i++) {
            if (ItemHandlerHelper.canItemStacksStack(stack, stacks.get(i))) {
                stacks.remove(i);
            }
        }
    }

    public void removeStack(int idx) {
        stacks.remove(idx);
    }

    public void removeTag(ResourceLocation id) {
        tags.remove(id);
    }

    public void addTag(ResourceLocation id) {
        tags.add(id);
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    public Set<ResourceLocation> getTags() {
        return tags;
    }

    public void markDirty() {
        ItemStack heldItem = filterGetter.get();
        if (!heldItem.isEmpty()) {
            CompoundNBT tagCompound = heldItem.getOrCreateTag();

            ListNBT itemList = new ListNBT();
            for (ItemStack stack : stacks) {
                CompoundNBT nbtTagCompound = new CompoundNBT();
                if (!stack.isEmpty()) {
                    stack.save(nbtTagCompound);
                }
                itemList.add(nbtTagCompound);
            }
            tagCompound.put("Items", itemList);

            ListNBT tagList = new ListNBT();
            for (ResourceLocation tag : tags) {
                tagList.add(StringNBT.valueOf(tag.toString()));
            }
            tagCompound.put("Tags", tagList);
        }
    }

}
