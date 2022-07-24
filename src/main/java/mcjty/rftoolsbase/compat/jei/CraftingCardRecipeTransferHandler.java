package mcjty.rftoolsbase.compat.jei;

import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardContainer;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class CraftingCardRecipeTransferHandler implements IRecipeTransferHandler<CraftingCardContainer, CraftingRecipe> {

    public static void register(IRecipeTransferRegistration transferRegistry) {
        transferRegistry.addRecipeTransferHandler(new CraftingCardRecipeTransferHandler(), RecipeTypes.CRAFTING);
    }

    @Override
    @Nonnull
    public Class<CraftingCardContainer> getContainerClass() {
        return CraftingCardContainer.class;
    }

    @Override
    public Optional<MenuType<CraftingCardContainer>> getMenuType() {
        return Optional.of(CraftingModule.CONTAINER_CRAFTING_CARD.get());
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return RecipeTypes.CRAFTING;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(CraftingCardContainer container, CraftingRecipe recipe, IRecipeSlotsView recipeLayout, Player player, boolean maxTransfer, boolean doTransfer) {
        List<IRecipeSlotView> slotViews = recipeLayout.getSlotViews();

        if (doTransfer) {
            RFToolsBaseJeiPlugin.transferRecipe(slotViews);
        }

        return null;
    }
}
