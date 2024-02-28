package mcjty.rftoolsbase.keys;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.setup.CommandHandler;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.porterNextDestination.consumeClick()) {
            RFToolsBaseMessages.sendToServer(CommandHandler.CMD_CYCLE_DESTINATION, TypedMap.builder().put(CommandHandler.PARAM_NEXT, true));
        } else if (KeyBindings.porterPrevDestination.consumeClick()) {
            RFToolsBaseMessages.sendToServer(CommandHandler.CMD_CYCLE_DESTINATION, TypedMap.builder().put(CommandHandler.PARAM_NEXT, false));
        }
    }
}
