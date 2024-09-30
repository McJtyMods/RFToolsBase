package mcjty.rftoolsbase.modules.filter.items;

import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        convertFromItem(stack);
    }

    public FilterModuleInventory(ItemStack filterItem) {
        filterGetter = () -> filterItem;
        convertFromItem(filterItem);
    }

    // Parameter is the filter item
    private void convertFromItem(ItemStack stack) {
        stacks.clear();
        tags.clear();
        if (stack.is(FilterModule.FILTER_MODULE.get())) {
            FilterModuleData data = stack.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
            stacks.addAll(data.stacks());
            data.tags().stream().map(TagTools::createItemTagKey).forEach(tags::add);
        }
    }

    public void addStack(ItemStack stack) {
        ItemStack toPlace = stack.copy();
        toPlace.setCount(1);
        for (ItemStack s : stacks) {
            if (ItemStack.isSameItemSameComponents(s, toPlace)) {
                return;
            }
        }
        stacks.add(toPlace);
    }

    public void removeStack(ItemStack stack) {
        for (int i = 0 ; i < stacks.size() ; i++) {
            if (ItemStack.isSameItemSameComponents(stack, stacks.get(i))) {
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
            FilterModuleData data = heldItem.getOrDefault(FilterModule.ITEM_FILTERMODULE_DATA, FilterModuleData.EMPTY);
            FilterModuleData newdata = new FilterModuleData(stacks, tags.stream().map(TagKey::location).toList(), data.blacklist(), data.damage(), data.components(), data.mod());
            heldItem.set(FilterModule.ITEM_FILTERMODULE_DATA, newdata);
        }
    }

}
