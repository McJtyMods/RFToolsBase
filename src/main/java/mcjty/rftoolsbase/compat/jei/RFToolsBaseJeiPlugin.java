package mcjty.rftoolsbase.compat.jei;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.network.RFToolsBaseMessages;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class RFToolsBaseJeiPlugin implements IModPlugin {

    public static void transferRecipe(Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients) {
        List<ItemStack> items = new ArrayList<>(10);
        for (int i = 0 ; i < 10 ; i++) {
            items.add(ItemStack.EMPTY);
        }
        for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : guiIngredients.entrySet()) {
            int recipeSlot = entry.getKey();
            List<ItemStack> allIngredients = entry.getValue().getAllIngredients();
            if (!allIngredients.isEmpty()) {
                items.set(recipeSlot, allIngredients.get(0));
            }
        }

        RFToolsBaseMessages.INSTANCE.sendToServer(new PacketSendRecipe(items));
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(RFToolsBase.MODID, "rftoolsbase");
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        CraftingCardRecipeTransferHandler.register(registration);
    }
}
