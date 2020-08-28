package mcjty.rftoolsbase.setup;


import mcjty.lib.modules.Modules;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

    public static final Builder SERVER_BUILDER = new Builder();
//    private static final Builder CLIENT_BUILDER = new Builder();

    public static ForgeConfigSpec SERVER_CONFIG;
//    public static ForgeConfigSpec CLIENT_CONFIG;


    public static String CATEGORY_GENERAL = "general";

    public static void register(Modules modules) {
        modules.initConfig();

        SERVER_CONFIG = SERVER_BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }

}
