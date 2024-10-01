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
import mcjty.rftoolsbase.setup.*;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

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

    public RFToolsBase(ModContainer mod, IEventBus bus, Dist dist) {
        instance = this;
        setupModules(bus);

        Config.register(mod, bus, modules);

        Registration.register(bus);

        bus.addListener(setup::init);
        bus.addListener(modules::init);
//        bus.addListener(setup::registerCapabilities);
        bus.addListener(this::onDataGen);
        bus.addListener(RFToolsBaseMessages::registerMessages);
        bus.addListener(setup.getBlockCapabilityRegistrar(Registration.RBLOCKS));

        NeoForge.EVENT_BUS.addListener((ServerStartedEvent e) -> TickOrderHandler.clean());
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
        modules.datagen(datagen, event.getLookupProvider());
        datagen.generate();
    }

    private void setupModules(IEventBus bus) {
        modules.register(new CraftingModule(bus));
        modules.register(new FilterModule(bus));
        modules.register(new InformationScreenModule());
        modules.register(new MachineInfuserModule(bus));
        modules.register(new TabletModule(bus));
        modules.register(new VariousModule());
        modules.register(new WorldGenModule());
    }
}
