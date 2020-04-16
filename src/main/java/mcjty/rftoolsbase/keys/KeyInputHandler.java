package mcjty.rftoolsbase.keys;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.setup.CommandHandler;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.porterNextDestination.isPressed()) {
            RFToolsBaseMessages.sendToServer(CommandHandler.CMD_CYCLE_DESTINATION, TypedMap.builder().put(CommandHandler.PARAM_NEXT, true));
        } else if (KeyBindings.porterPrevDestination.isPressed()) {
            RFToolsBaseMessages.sendToServer(CommandHandler.CMD_CYCLE_DESTINATION, TypedMap.builder().put(CommandHandler.PARAM_NEXT, false));
        }
    }
}
