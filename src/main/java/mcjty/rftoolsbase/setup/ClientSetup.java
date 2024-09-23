package mcjty.rftoolsbase.setup;


import mcjty.rftoolsbase.client.RenderWorldLastEventHandler;
import mcjty.rftoolsbase.keys.KeyBindings;
import mcjty.rftoolsbase.keys.KeyInputHandler;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientSetup {

    public static void init(FMLClientSetupEvent e) {
        NeoForge.EVENT_BUS.addListener(RenderWorldLastEventHandler::tick);
        NeoForge.EVENT_BUS.register(new KeyInputHandler());
    }

    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        KeyBindings.init(event);
    }
}
