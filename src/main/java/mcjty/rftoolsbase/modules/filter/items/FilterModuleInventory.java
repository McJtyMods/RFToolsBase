package mcjty.rftoolsbase.modules.filter.items;

import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.TagTools;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class FilterModuleInventory {

    private final ItemStackList stacks = ItemStackList.create();
    private final Set<TagKey<Item>> tags = new HashSet<>();

    private final Supplier<ItemStack> filterGetter;

    public FilterModuleInventory(Player player) {
        filterGetter = () -> player.getItemInHand(InteractionHand.MAIN_HAND);
        CompoundTag tagCompound = player.getItemInHand(InteractionHand.MAIN_HAND).getOrCreateTag();
        convertFromNBT(tagCompound);
    }

    public FilterModuleInventory(ItemStack filterItem) {
        filterGetter = () -> filterItem;
        CompoundTag tagCompound = filterItem.getOrCreateTag();
        convertFromNBT(tagCompound);
    }

    private void convertFromNBT(CompoundTag tagCompound) {
        ListTag itemList = tagCompound.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag compound = itemList.getCompound(i);
            ItemStack s = ItemStack.of(compound);
            if (!s.isEmpty()) {
                stacks.add(s);
            }
        }
        ListTag tagList = tagCompound.getList("Tags", Tag.TAG_STRING);
        for (int i = 0 ; i < tagList.size() ; i++) {
            String s = tagList.getString(i);
            tags.add(TagTools.createItemTagKey(new ResourceLocation(s)));
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

    public void removeTag(TagKey<Item> id) {
        tags.remove(id);
    }

    public void addTag(TagKey<Item> id) {
        tags.add(id);
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    public Set<TagKey<Item>> getTags() {
        return tags;
    }

    public void markDirty() {
        ItemStack heldItem = filterGetter.get();
        if (!heldItem.isEmpty()) {
            CompoundTag tagCompound = heldItem.getOrCreateTag();

            ListTag itemList = new ListTag();
            for (ItemStack stack : stacks) {
                CompoundTag nbtTagCompound = new CompoundTag();
                if (!stack.isEmpty()) {
                    stack.save(nbtTagCompound);
                }
                itemList.add(nbtTagCompound);
            }
            tagCompound.put("Items", itemList);

            ListTag tagList = new ListTag();
            for (TagKey<Item> tag : tags) {
                tagList.add(StringTag.valueOf(tag.location().toString()));
            }
            tagCompound.put("Tags", tagList);
        }
    }

}
