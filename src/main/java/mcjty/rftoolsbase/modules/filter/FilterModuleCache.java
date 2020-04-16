package mcjty.rftoolsbase.modules.filter;

import mcjty.lib.varia.ItemStackList;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class FilterModuleCache implements Predicate<ItemStack> {
    private boolean matchDamage = true;
    private boolean blacklistMode = true;
    private boolean nbtMode = false;
    private boolean modMode = false;
    private ItemStackList stacks;
    private Set<ResourceLocation> tags = Collections.emptySet();

    // Parameter is the filter item.
    public FilterModuleCache(ItemStack stack) {
        CompoundNBT tagCompound = stack.getTag();
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
                modName = stack.getItem().getRegistryName().getNamespace();
            }

            if (!tags.isEmpty()) {
                for (ResourceLocation tag : stack.getItem().getTags()) {
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
                if (matchDamage && itemStack.getDamage() != stack.getDamage()) {
                    continue;
                }
                if (nbtMode && !ItemStack.areItemStackTagsEqual(itemStack, stack)) {
                    continue;
                }
                if (modMode) {
                    if (modName.equals(itemStack.getItem().getRegistryName().getNamespace())) {
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
