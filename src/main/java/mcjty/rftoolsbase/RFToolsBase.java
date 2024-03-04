package mcjty.rftoolsbase;

import mcjty.lib.datagen.DataGen;
import mcjty.lib.modules.Modules;
import mcjty.rftoolsbase.client.ClientInfo;
import mcjty.rftoolsbase.modules.crafting.CraftingModule;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.tablet.TabletModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import mcjty.rftoolsbase.setup.ClientSetup;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.ModSetup;
import mcjty.rftoolsbase.setup.Registration;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.fml.loading.FMLEnvironment;

import java.util.function.Supplier;


@Mod(RFToolsBase.MODID)
public class RFToolsBase {

    public static final String MODID = "rftoolsbase";

    @SuppressWarnings("PublicField")
    public static final ModSetup setup = new ModSetup();

    @SuppressWarnings("PublicField")
    public static RFToolsBase instance;
    private final Modules modules = new Modules();
    public ClientInfo clientInfo = new ClientInfo();

    public RFToolsBase() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Dist dist = FMLEnvironment.dist;

        instance = this;
        setupModules();

        Config.register(bus, modules);

        Registration.register(bus);

        bus.addListener(setup::init);
        bus.addListener(modules::init);
        bus.addListener(setup::registerCapabilities);
        bus.addListener(this::onDataGen);

        MinecraftForge.EVENT_BUS.addListener((ServerStartedEvent e) -> TickOrderHandler.clean());
        if (dist.isClient()) {
            bus.addListener(ClientSetup::init);
            bus.addListener(ClientSetup::registerKeyBinds);
            bus.addListener(modules::initClient);
        }
    }

    public static <T extends Item> Supplier<T> tab(Supplier<T> supplier) {
        return instance.setup.tab(supplier);
    }

    private void onDataGen(GatherDataEvent event) {
        DataGen datagen = new DataGen(MODID, event);
        modules.datagen(datagen);
        datagen.generate();
    }

    private void setupModules() {
        modules.register(new CraftingModule());
        modules.register(new FilterModule());
        modules.register(new InformationScreenModule());
        modules.register(new MachineInfuserModule());
        modules.register(new TabletModule());
        modules.register(new VariousModule());
        modules.register(new WorldGenModule());
    }

}
