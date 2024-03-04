package mcjty.rftoolsbase.setup;


import mcjty.lib.modules.Modules;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fml.ModLoadingContext;
import net.neoforged.neoforge.fml.config.ModConfig;

public class Config {

    public static final Builder SERVER_BUILDER = new Builder();
    public static final Builder COMMON_BUILDER = new Builder();
//    private static final Builder CLIENT_BUILDER = new Builder();

    public static ModConfigSpec SERVER_CONFIG;
    public static ModConfigSpec COMMON_CONFIG;
//    public static ModConfigSpec CLIENT_CONFIG;


    public static void register(IEventBus bus, Modules modules) {
        modules.initConfig(bus);

        SERVER_CONFIG = SERVER_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }

}
