package mcjty.rftoolsbase.api.compat;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface JEIRecipeAcceptor {

    void setGridContents(List<ItemStack> stacks);
}
