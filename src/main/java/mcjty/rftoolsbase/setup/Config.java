package mcjty.rftoolsbase.setup;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserConfiguration;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class Config {

    private static final Builder COMMON_BUILDER = new Builder();
//    private static final Builder CLIENT_BUILDER = new Builder();

    public static final ForgeConfigSpec COMMON_CONFIG;
//    public static final ForgeConfigSpec CLIENT_CONFIG;


    public static String CATEGORY_WORLDGEN = "worldgen";
    public static String CATEGORY_GENERAL = "general";
    public static String SUB_CATEGORY_OVERWORLD = "overworld";
    public static String SUB_CATEGORY_NETHER = "nether";

    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_CHANCES;
    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_VEINSIZE;
    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_MINY;
    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_MAXY;

    public static ForgeConfigSpec.IntValue NETHER_ORE_CHANCES;
    public static ForgeConfigSpec.IntValue NETHER_ORE_VEINSIZE;
    public static ForgeConfigSpec.IntValue NETHER_ORE_MINY;
    public static ForgeConfigSpec.IntValue NETHER_ORE_MAXY;

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

    static {
        setupWorldgenConfig();
        MachineInfuserConfiguration.init(COMMON_BUILDER);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void setupWorldgenConfig() {
        COMMON_BUILDER.comment("Dimensional shard ore generation").push(CATEGORY_WORLDGEN);

        COMMON_BUILDER.comment("Overworld").push(SUB_CATEGORY_OVERWORLD);
        OVERWORLD_ORE_CHANCES = COMMON_BUILDER
                .comment("Number of times to try generate the ore (set to 0 to disable)")
                .defineInRange("oreChances", 1, 0, 256);
        OVERWORLD_ORE_VEINSIZE = COMMON_BUILDER
                .comment("Max size of veins")
                .defineInRange("oreVeinsize", 3, 1, 256);
        OVERWORLD_ORE_MINY = COMMON_BUILDER
                .comment("Min height")
                .defineInRange("oreMin", 2, 0, 256);
        OVERWORLD_ORE_MAXY = COMMON_BUILDER
                .comment("Max height")
                .defineInRange("oreMax", 40, 0, 256);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Overworld").push(SUB_CATEGORY_NETHER);
        NETHER_ORE_CHANCES = COMMON_BUILDER
                .comment("Number of times to try generate the ore (set to 0 to disable)")
                .defineInRange("oreChances", 8, 0, 256);
        NETHER_ORE_VEINSIZE = COMMON_BUILDER
                .comment("Max size of veins")
                .defineInRange("oreVeinsize", 8, 1, 256);
        NETHER_ORE_MINY = COMMON_BUILDER
                .comment("Min height")
                .defineInRange("oreMin", 2, 0, 256);
        NETHER_ORE_MAXY = COMMON_BUILDER
                .comment("Max height")
                .defineInRange("oreMax", 40, 0, 256);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();
    }

}
