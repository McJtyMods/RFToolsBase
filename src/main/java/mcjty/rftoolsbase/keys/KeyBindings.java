package mcjty.rftoolsbase.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;


public class KeyBindings {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("rftoolsbase", "rftools"));

    public static KeyMapping porterNextDestination;
    public static KeyMapping porterPrevDestination;

    public static void init() {
        porterNextDestination = new KeyMapping("key.nextDestination", InputConstants.Type.KEYSYM, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_BRACKET, CATEGORY);
        net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping(porterNextDestination);
        porterPrevDestination = new KeyMapping("key.prevDestination", InputConstants.Type.KEYSYM, org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_BRACKET, CATEGORY);
        net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper.registerKeyMapping(porterPrevDestination);
//        debugDumpNBTItem = new KeyBinding("key.debugDumpNBTItem", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, CATEGORY);
//        ClientRegistry.registerKeyBinding(debugDumpNBTItem);
//        debugDumpNBTBlock = new KeyBinding("key.debugDumpNBTBlock", KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, CATEGORY);
//        ClientRegistry.registerKeyBinding(debugDumpNBTBlock);
    }
}
