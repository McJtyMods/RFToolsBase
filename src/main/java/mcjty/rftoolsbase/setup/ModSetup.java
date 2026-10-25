package mcjty.rftoolsbase.setup;

import mcjty.lib.McJtyLib;
import mcjty.lib.setup.DefaultModSetup;
import mcjty.rftoolsbase.modules.hud.Hud;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.network.FriendlyByteBuf;

public class ModSetup extends DefaultModSetup {

    @Override
    public void init() {
        super.init();
        CommandHandler.registerCommands();
        // Needs to be here: after registration of everything and after reading config
        McJtyLib.registerListCommandInfo(Hud.COMMAND_GETHUDLOG, String.class, buf -> buf.readUtf(32767), FriendlyByteBuf::writeUtf);

        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_LEVEL_TICK.register(level -> TickOrderHandler.postWorldTick(level.dimension()));
    }

    @Override
    protected void setupModCompat() {
    }
}
