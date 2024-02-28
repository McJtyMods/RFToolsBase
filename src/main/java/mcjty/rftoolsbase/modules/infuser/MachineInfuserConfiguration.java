package mcjty.rftoolsbase.modules.infuser;

import net.neoforged.neoforge.common.ForgeConfigSpec;

public class MachineInfuserConfiguration {

    public static final String CATEGORY_INFUSER = "infuser";

    public static ForgeConfigSpec.IntValue MAX_INFUSE;
    public static ForgeConfigSpec.IntValue MAXENERGY;
    public static ForgeConfigSpec.IntValue RECEIVEPERTICK;
    public static ForgeConfigSpec.IntValue RFPERTICK;

    public static void init(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Settings for the infusing system").push(CATEGORY_INFUSER);

        MAX_INFUSE = SERVER_BUILDER
                .comment("Maximum amount of dimensional shards before a machine is fully infused")
                .defineInRange("maxInfuse", 256, 1, Integer.MAX_VALUE);

        RFPERTICK = SERVER_BUILDER
                .comment("Amount of RF used per tick while infusing")
                .defineInRange("usePerTick", 600, 0, Integer.MAX_VALUE);
        MAXENERGY = SERVER_BUILDER
                .comment("Maximum RF storage that the infuser can hold")
                .defineInRange("infuserMaxRF", 60000, 0, Integer.MAX_VALUE);
        RECEIVEPERTICK = SERVER_BUILDER
                .comment("RF per tick that the infuser can receive")
                .defineInRange("infuserRFPerTick", 600, 0, Integer.MAX_VALUE);

        SERVER_BUILDER.pop();
    }
}
