package mcjty.rftoolsbase.modules.infuser;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MachineInfuserConfiguration {

    public static final String CATEGORY_INFUSER = "infuser";

    public static ModConfigSpec.IntValue MAX_INFUSE;
    public static ModConfigSpec.IntValue MAXENERGY;
    public static ModConfigSpec.IntValue RECEIVEPERTICK;
    public static ModConfigSpec.IntValue RFPERTICK;

    public static void init(ModConfigSpec.Builder SERVER_BUILDER) {
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
