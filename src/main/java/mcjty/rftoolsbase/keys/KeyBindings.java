package mcjty.rftoolsbase.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;


public class KeyBindings {

    public static KeyMapping porterNextDestination;
    public static KeyMapping porterPrevDestination;

    public static void init() {
        porterNextDestination = new KeyMapping("key.nextDestination", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.right.bracket"), "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(porterNextDestination);
        porterPrevDestination = new KeyMapping("key.prevDestination", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.left.bracket"), "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(porterPrevDestination);
//        debugDumpNBTItem = new KeyBinding("key.debugDumpNBTItem", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(debugDumpNBTItem);
//        debugDumpNBTBlock = new KeyBinding("key.debugDumpNBTBlock", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(debugDumpNBTBlock);
    }
}
