package mcjty.rftoolsbase.compat.jei;

import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;

import javax.annotation.Nonnull;
import java.util.Map;

public class CraftingCardRecipeTransferHandler implements IRecipeTransferHandler<CraftingCardContainer, CraftingRecipe> {

    public static void register(IRecipeTransferRegistration transferRegistry) {
        transferRegistry.addRecipeTransferHandler(new CraftingCardRecipeTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    @Nonnull
    public Class<CraftingCardContainer> getContainerClass() {
        return CraftingCardContainer.class;
    }


    @Override
    public Class<CraftingRecipe> getRecipeClass() {
        return CraftingRecipe.class;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public IRecipeTransferError transferRecipe(CraftingCardContainer container, CraftingRecipe recipe, IRecipeLayout recipeLayout, Player player, boolean maxTransfer, boolean doTransfer) {
        Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();

        if (doTransfer) {
            RFToolsBaseJeiPlugin.transferRecipe(guiIngredients);
        }

        return null;
    }
}
