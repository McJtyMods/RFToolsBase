package mcjty.rftoolsbase.compat.jei;

import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class CraftingCardRecipeTransferHandler implements IRecipeTransferHandler<CraftingCardContainer> {

    public static void register(IRecipeTransferRegistration transferRegistry) {
        transferRegistry.addRecipeTransferHandler(new CraftingCardRecipeTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    @Nonnull
    public Class<CraftingCardContainer> getContainerClass() {
        return CraftingCardContainer.class;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(@Nonnull CraftingCardContainer craftingCardContainer, IRecipeLayout recipeLayout, @Nonnull PlayerEntity playerEntity, boolean maxTransfer, boolean doTransfer) {
        Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();

        if (doTransfer) {
            RFToolsBaseJeiPlugin.transferRecipe(guiIngredients);
        }

        return null;
    }
}
