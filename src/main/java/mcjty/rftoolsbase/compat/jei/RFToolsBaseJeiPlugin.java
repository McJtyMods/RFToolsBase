package mcjty.rftoolsbase.compat.jei;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class RFToolsBaseJeiPlugin implements IModPlugin {

    public static void transferRecipe(List<IRecipeSlotView> slotViews) {
        List<ItemStack> items = new ArrayList<>(10);
        for (int i = 0 ; i < 10 ; i++) {
            items.add(ItemStack.EMPTY);
        }
        for (int i = 0 ; i < slotViews.size() ; i++) {
            List<ITypedIngredient<?>> allIngredients = slotViews.get(i).getAllIngredients().collect(Collectors.toList());
            if (!allIngredients.isEmpty()) {
                ItemStack stack = allIngredients.get(0).getIngredient(VanillaTypes.ITEM_STACK).get();
                items.set(i, stack);
            }
        }
        RFToolsBaseMessages.sendToServer(PacketSendRecipe.create(items));
    }

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "rftoolsbase");
    }

    @Override
    public void registerRecipeTransferHandlers(@Nonnull IRecipeTransferRegistration registration) {
        CraftingCardRecipeTransferHandler.register(registration);
    }
}
