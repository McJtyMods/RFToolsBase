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
import mcjty.rftoolsbase.setup.ClientSetup;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.ModSetup;
import mcjty.rftoolsbase.setup.Registration;
import mcjty.rftoolsbase.tools.TickOrderHandler;
import mcjty.rftoolsbase.worldgen.OreGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


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
        instance = this;
        setupModules();

        Config.register(modules);

        Registration.register();

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(setup::init);
        modbus.addListener(modules::init);
        modbus.addListener(setup::registerCapabilities);
        MinecraftForge.EVENT_BUS.addListener((ServerStartedEvent e) -> TickOrderHandler.clean());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGenerator::onBiomeLoadingEvent);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modbus.addListener(ClientSetup::init);
            modbus.addListener(modules::initClient);
        });
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
