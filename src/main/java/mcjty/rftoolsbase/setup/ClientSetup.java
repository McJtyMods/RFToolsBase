package mcjty.rftoolsbase.setup;


import mcjty.rftoolsbase.client.RenderWorldLastEventHandler;
import mcjty.rftoolsbase.keys.KeyBindings;
import mcjty.rftoolsbase.keys.KeyInputHandler;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.MinecraftForge;
import net.neoforged.neoforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.addListener(RenderWorldLastEventHandler::tick);
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
    }

    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        KeyBindings.init(event);
    }
}
