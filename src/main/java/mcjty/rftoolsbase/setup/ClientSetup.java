package mcjty.rftoolsbase.setup;


import mcjty.rftoolsbase.client.RenderWorldLastEventHandler;
import mcjty.rftoolsbase.keys.KeyBindings;
import mcjty.rftoolsbase.keys.KeyInputHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.addListener(RenderWorldLastEventHandler::tick);
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
    }

    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        KeyBindings.init(event);
    }
}
