package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.items.CraftingCardItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class CommandHandler {

    public static final String CMD_TESTRECIPE = "testRecipe";

    public static void registerCommands() {
        McJtyLib.registerCommand(RFToolsBase.MODID, CMD_TESTRECIPE, (player, arguments) -> {
            ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return false;
            }
            if (heldItem.getItem() instanceof CraftingCardItem) {
                CraftingCardItem.testRecipe(player.getEntityWorld(), heldItem);
            }
            return true;
        });
    }
}
