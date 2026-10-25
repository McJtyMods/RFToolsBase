package mcjty.rftoolsbase.setup;


import mcjty.lib.modules.Modules;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class Config {

    public static final Builder SERVER_BUILDER = new Builder();
    public static final Builder COMMON_BUILDER = new Builder();
//    private static final Builder CLIENT_BUILDER = new Builder();

    public static ModConfigSpec SERVER_CONFIG;
    public static ModConfigSpec COMMON_CONFIG;
//    public static ModConfigSpec CLIENT_CONFIG;


    public static void register(Modules modules) {
        modules.initConfig();

        SERVER_CONFIG = SERVER_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();

        fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry.INSTANCE.register("rftoolsbase", ModConfig.Type.SERVER, SERVER_CONFIG);
        fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry.INSTANCE.register("rftoolsbase", ModConfig.Type.COMMON, COMMON_CONFIG);
    }

}
