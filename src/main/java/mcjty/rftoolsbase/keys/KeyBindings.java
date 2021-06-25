package mcjty.rftoolsbase.keys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;


public class KeyBindings {

    public static KeyBinding porterNextDestination;
    public static KeyBinding porterPrevDestination;

    public static void init() {
        porterNextDestination = new KeyBinding("key.nextDestination", KeyConflictContext.IN_GAME, InputMappings.getKey("key.keyboard.right.bracket"), "key.categories.rftools");
        ClientRegistry.registerKeyBinding(porterNextDestination);
        porterPrevDestination = new KeyBinding("key.prevDestination", KeyConflictContext.IN_GAME, InputMappings.getKey("key.keyboard.left.bracket"), "key.categories.rftools");
        ClientRegistry.registerKeyBinding(porterPrevDestination);
//        debugDumpNBTItem = new KeyBinding("key.debugDumpNBTItem", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(debugDumpNBTItem);
//        debugDumpNBTBlock = new KeyBinding("key.debugDumpNBTBlock", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.rftools");
//        ClientRegistry.registerKeyBinding(debugDumpNBTBlock);
    }
}
