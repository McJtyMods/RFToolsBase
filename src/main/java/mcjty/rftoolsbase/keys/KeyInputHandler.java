package mcjty.rftoolsbase.keys;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.setup.CommandHandler;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;

public class KeyInputHandler {

    public void onKeyInput() {
        if (KeyBindings.porterNextDestination.consumeClick()) {
            RFToolsBaseMessages.sendToServer(CommandHandler.CMD_CYCLE_DESTINATION, TypedMap.builder().put(CommandHandler.PARAM_NEXT, true));
        } else if (KeyBindings.porterPrevDestination.consumeClick()) {
            RFToolsBaseMessages.sendToServer(CommandHandler.CMD_CYCLE_DESTINATION, TypedMap.builder().put(CommandHandler.PARAM_NEXT, false));
        }
    }
}
