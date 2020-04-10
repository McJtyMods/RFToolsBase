package mcjty.rftoolsbase.modules.various;

import mcjty.lib.varia.ItemStackList;
import mcjty.lib.varia.ItemStackTools;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FilterModuleCache {
    private boolean matchDamage = true;
    private boolean commonTagsMode = false;
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
            commonTagsMode = tagCompound.getBoolean("commonTagMode");
            nbtMode = tagCompound.getBoolean("nbtMode");
            modMode = tagCompound.getBoolean("modMode");
            blacklistMode = "Black".equals(tagCompound.getString("blacklistMode"));
            tags = new HashSet<>();
            ListNBT bufferTagList = tagCompound.getList("Items", Constants.NBT.TAG_COMPOUND);
            int cnt = 0;
            for (int i = 0 ; i < bufferTagList.size() ; i++) {
                CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.read(nbtTagCompound);
                if (!s.isEmpty()) {
                    cnt++;
                }
            }
            stacks = ItemStackList.create(cnt);
            cnt = 0;
            for (int i = 0 ; i < bufferTagList.size() ; i++) {
                CompoundNBT nbtTagCompound = bufferTagList.getCompound(i);
                ItemStack s = ItemStack.read(nbtTagCompound);
                if (!s.isEmpty()) {
                    stacks.set(cnt++, s);
                    if (commonTagsMode) {
                        ItemStackTools.addCommonTags(s.getItem().getTags(), tags);
                    }
                }
            }
        } else {
            stacks = ItemStackList.EMPTY;
        }
    }

    public boolean match(ItemStack stack) {
        if (!stack.isEmpty()) {
            boolean match = false;
            String modName = "";
            if (modMode) {
                modName = stack.getItem().getRegistryName().getNamespace();
            }

            if (commonTagsMode) {
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
            } else {
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
