package mcjty.rftoolsbase.setup;


import mcjty.lib.modules.Modules;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

    public static final Builder SERVER_BUILDER = new Builder();
    public static final Builder COMMON_BUILDER = new Builder();
//    private static final Builder CLIENT_BUILDER = new Builder();

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;
//    public static ForgeConfigSpec CLIENT_CONFIG;


    public static void register(IEventBus bus, Modules modules) {
        modules.initConfig(bus);

        SERVER_CONFIG = SERVER_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }

}
