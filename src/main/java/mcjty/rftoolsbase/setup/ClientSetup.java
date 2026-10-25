package mcjty.rftoolsbase.setup;

public class ClientSetup implements net.fabricmc.api.ClientModInitializer {
    @Override public void onInitializeClient() {
        ClientMessages.register();
        mcjty.rftoolsbase.RFToolsBase.instance.modules.initClient();
        mcjty.rftoolsbase.keys.KeyBindings.init();
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.screen == null) new mcjty.rftoolsbase.keys.KeyInputHandler().onKeyInput();
        });
        net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.COLLECT_SUBMITS.register(mcjty.rftoolsbase.client.RenderWorldLastEventHandler::tick);
    }
}
