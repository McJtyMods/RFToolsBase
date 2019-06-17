package mcjty.rftoolsbase;

import mcjty.lib.base.ModBase;
import mcjty.lib.setup.IProxy;
import mcjty.rftoolsbase.config.Config;
import mcjty.rftoolsbase.setup.ClientProxy;
import mcjty.rftoolsbase.setup.ModSetup;
import mcjty.rftoolsbase.setup.ServerProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;


@Mod(RFToolsBase.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RFToolsBase implements ModBase {

    public static final String MODID = "rftoolsbase";

    @SuppressWarnings("PublicField")
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    @SuppressWarnings("PublicField")
    public static ModSetup setup = new ModSetup();

    public RFToolsBase() {
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);


        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);

//        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("theoneprobe-client.toml"));
        Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve("rftoolsbase-server.toml"));
    }

    public void init(final FMLCommonSetupEvent event) {
        setup.init(event);
        proxy.init(event);
    }


    @Override
    public String getModId() {
        return RFToolsBase.MODID;
    }

    @Override
    public void openManual(PlayerEntity entityPlayer, int i, String s) {
        // @todo
    }
}
