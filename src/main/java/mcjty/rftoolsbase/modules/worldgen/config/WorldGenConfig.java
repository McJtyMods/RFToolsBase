package mcjty.rftoolsbase.modules.worldgen.config;

import mcjty.rftoolsbase.setup.Config;
import net.minecraftforge.common.ForgeConfigSpec;

public class WorldGenConfig {

    public static String CATEGORY_WORLDGEN = "worldgen";
    public static String SUB_CATEGORY_OVERWORLD = "overworld";
    public static String SUB_CATEGORY_NETHER = "nether";
    public static String SUB_CATEGORY_END = "end";

    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_CHANCES;
    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_VEINSIZE;
    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_MINY;
    public static ForgeConfigSpec.IntValue OVERWORLD_ORE_MAXY;
    public static ForgeConfigSpec.IntValue NETHER_ORE_CHANCES;
    public static ForgeConfigSpec.IntValue NETHER_ORE_VEINSIZE;
    public static ForgeConfigSpec.IntValue NETHER_ORE_MINY;
    public static ForgeConfigSpec.IntValue NETHER_ORE_MAXY;
    public static ForgeConfigSpec.IntValue END_ORE_CHANCES;
    public static ForgeConfigSpec.IntValue END_ORE_VEINSIZE;
    public static ForgeConfigSpec.IntValue END_ORE_MINY;
    public static ForgeConfigSpec.IntValue END_ORE_MAXY;

    public static void setupWorldgenConfig() {
        Config.COMMON_BUILDER.comment("Dimensional shard ore generation").push(CATEGORY_WORLDGEN);

        Config.COMMON_BUILDER.comment("Overworld").push(SUB_CATEGORY_OVERWORLD);
        OVERWORLD_ORE_CHANCES = Config.COMMON_BUILDER
                .comment("Number of times to try generate the ore (set to 0 to disable)")
                .defineInRange("oreChances", 2, 0, 256);
        OVERWORLD_ORE_VEINSIZE = Config.COMMON_BUILDER
                .comment("Max size of veins")
                .defineInRange("oreVeinsize", 5, 1, 256);
        OVERWORLD_ORE_MINY = Config.COMMON_BUILDER
                .comment("Min height")
                .defineInRange("oreMin", 2, 0, 256);
        OVERWORLD_ORE_MAXY = Config.COMMON_BUILDER
                .comment("Max height")
                .defineInRange("oreMax", 40, 0, 256);
        Config.COMMON_BUILDER.pop();

        Config.COMMON_BUILDER.comment("Overworld").push(SUB_CATEGORY_NETHER);
        NETHER_ORE_CHANCES = Config.COMMON_BUILDER
                .comment("Number of times to try generate the ore (set to 0 to disable)")
                .defineInRange("oreChances", 8, 0, 256);
        NETHER_ORE_VEINSIZE = Config.COMMON_BUILDER
                .comment("Max size of veins")
                .defineInRange("oreVeinsize", 8, 1, 256);
        NETHER_ORE_MINY = Config.COMMON_BUILDER
                .comment("Min height")
                .defineInRange("oreMin", 2, 0, 256);
        NETHER_ORE_MAXY = Config.COMMON_BUILDER
                .comment("Max height")
                .defineInRange("oreMax", 40, 0, 256);
        Config.COMMON_BUILDER.pop();

        Config.COMMON_BUILDER.comment("End").push(SUB_CATEGORY_END);
        END_ORE_CHANCES = Config.COMMON_BUILDER
                .comment("Number of times to try generate the ore (set to 0 to disable)")
                .defineInRange("oreChances", 8, 0, 256);
        END_ORE_VEINSIZE = Config.COMMON_BUILDER
                .comment("Max size of veins")
                .defineInRange("oreVeinsize", 10, 1, 256);
        END_ORE_MINY = Config.COMMON_BUILDER
                .comment("Min height")
                .defineInRange("oreMin", 2, 0, 256);
        END_ORE_MAXY = Config.COMMON_BUILDER
                .comment("Max height")
                .defineInRange("oreMax", 80, 0, 256);
        Config.COMMON_BUILDER.pop();

        Config.COMMON_BUILDER.pop();
    }
}
