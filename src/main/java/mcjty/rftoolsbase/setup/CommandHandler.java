package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.various.IItemCycler;
import mcjty.rftoolsbase.modules.crafting.items.CraftingCardItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class CommandHandler {

    public static final String CMD_TESTRECIPE = "testRecipe";

    public static final String CMD_CYCLE_DESTINATION = "cycleDestination";
    public static final Key<Boolean> PARAM_NEXT = new Key<>("next", Type.BOOLEAN);

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
        McJtyLib.registerCommand(RFToolsBase.MODID, CMD_CYCLE_DESTINATION, (player, arguments) -> {
            ItemStack heldItem = player.getHeldItem(player.getActiveHand() == null ? Hand.MAIN_HAND : player.getActiveHand());
            if (heldItem.getItem() instanceof IItemCycler) {
                ((IItemCycler) heldItem.getItem()).cycle(player, heldItem, arguments.get(PARAM_NEXT));
            }
            return true;
        });


    }
}
