package mcjty.rftoolsbase;

import mcjty.lib.base.ModBase;
import mcjty.rftoolsbase.config.Config;
import mcjty.rftoolsbase.setup.ModSetup;
import net.minecraft.entity.player.PlayerEntity;
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
    public static ModSetup setup = new ModSetup();

    @SuppressWarnings("PublicField")
    public static RFToolsBase instance;

    public RFToolsBase() {
        instance = this;

//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent e) -> setup.init(e));

//        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("theoneprobe-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("rftoolsbase-common.toml"));
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
