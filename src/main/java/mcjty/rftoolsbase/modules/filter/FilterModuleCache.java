package mcjty.rftoolsbase.modules.filter;

import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.TagTools;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class FilterModuleCache implements Predicate<ItemStack> {
    private boolean matchDamage = true;
    private boolean blacklistMode = true;
    private boolean nbtMode = false;
    private boolean modMode = false;
    private final ItemStackList stacks;
    private Set<TagKey<Item>> tags = Collections.emptySet();

    // Parameter is the filter item.
    public FilterModuleCache(ItemStack stack) {
        CompoundTag tagCompound = stack.getTag();
        if (tagCompound != null) {
            matchDamage = tagCompound.getBoolean("damageMode");
            nbtMode = tagCompound.getBoolean("nbtMode");
            modMode = tagCompound.getBoolean("modMode");
            blacklistMode = "Black".equals(tagCompound.getString("blacklistMode"));

            FilterModuleInventory inventory = new FilterModuleInventory(stack);
            tags = new HashSet<>();
            stacks = ItemStackList.create();
            stacks.addAll(inventory.getStacks());
            tags.addAll(inventory.getTags());
        } else {
            stacks = ItemStackList.EMPTY;
        }
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!stack.isEmpty()) {
            boolean match = false;
            String modName = "";
            if (modMode) {
                modName = Tools.getId(stack).getNamespace();
            }

            if (!tags.isEmpty()) {
                // @todo not entirey optimal
                for (TagKey<Item> tag : TagTools.getTags(stack.getItem())) {
                    if (tags.contains(tag)) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) {
                match = itemMatches(stack, modName);
            }
            return match != blacklistMode;
        }
        return false;
    }

    private boolean itemMatches(ItemStack stack, String modName) {
        if (stacks != null) {
            for (ItemStack itemStack : stacks) {
                if (matchDamage && itemStack.getDamageValue() != stack.getDamageValue()) {
                    continue;
                }
                if (nbtMode && !ItemStack.isSameItemSameTags(itemStack, stack)) {
                    continue;
                }
                if (modMode) {
                    if (modName.equals(Tools.getId(itemStack).getNamespace())) {
                        return true;
                    }
                } else if (itemStack.getItem().equals(stack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }
}
