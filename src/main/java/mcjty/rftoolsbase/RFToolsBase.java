package mcjty.rftoolsbase;

import mcjty.lib.modules.Modules;
import mcjty.rftoolsbase.client.ClientInfo;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.setup.*;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public class RFToolsBase implements net.fabricmc.api.ModInitializer {
    public static final String MODID = "rftoolsbase";
    public static final ModSetup setup = new ModSetup();
    public static RFToolsBase instance;
    public final Modules modules = new Modules();
    public ClientInfo clientInfo = new ClientInfo();
    @Override
    public void onInitialize() {
        instance = this;
        modules.register(new CraftingModule());
        modules.register(new FilterModule());
        modules.register(new InformationScreenModule());
        modules.register(new MachineInfuserModule());
        modules.register(new TabletModule());
        modules.register(new VariousModule());
        modules.register(new WorldGenModule());
        Config.register(modules);
        Registration.register();
        setup.init();
        modules.init();
        setup.registerBlockCapabilities(Registration.RBLOCKS);
        RFToolsBaseMessages.registerMessages();
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED.register(server -> TickOrderHandler.clean());
    }
    public static <T extends Item> java.util.function.Function<Item.Properties, T> tab(java.util.function.Function<Item.Properties, T> factory) {
        return properties -> instance.setup.tab(() -> factory.apply(properties)).get();
    }

    public static <T extends Item> Supplier<T> tab(Supplier<T> supplier) {
        return instance.setup.tab(supplier);
    }

}
