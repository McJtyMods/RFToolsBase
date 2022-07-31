package mcjty.rftoolsbase.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;


public class KeyBindings {

    public static KeyMapping porterNextDestination;
    public static KeyMapping porterPrevDestination;

    public static void init(RegisterKeyMappingsEvent event) {
        porterNextDestination = new KeyMapping("key.nextDestination", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.right.bracket"), "key.categories.rftools");
        event.register(porterNextDestination);
        porterPrevDestination = new KeyMapping("key.prevDestination", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.left.bracket"), "key.categories.rftools");
        event.register(porterPrevDestination);
//        debugDumpNBTItem = new KeyBinding("key.debugDumpNBTItem", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(debugDumpNBTItem);
//        debugDumpNBTBlock = new KeyBinding("key.debugDumpNBTBlock", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(debugDumpNBTBlock);
    }
}
