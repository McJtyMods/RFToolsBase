package mcjty.rftoolsbase.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class CountPlacementConfig implements DecoratorConfiguration {

    public static final Codec<CountPlacementConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                Codec.INT.fieldOf("bottom_offset").orElse(0).forGetter((config) -> config.bottomOffset),
                Codec.INT.fieldOf("top_offset").orElse(0).forGetter((config) -> config.topOffset),
                Codec.INT.fieldOf("maximum").orElse(0).forGetter((config) -> config.maximum),
                Codec.INT.fieldOf("tries").orElse(0).forGetter((config) -> config.tries))
                .apply(instance, CountPlacementConfig::new);
    });

    public final int tries;
    public final int bottomOffset;
    public final int topOffset;
    public final int maximum;

    public CountPlacementConfig(int bottomOffset, int topOffset, int maximum, int tries) {
        this.bottomOffset = bottomOffset;
        this.topOffset = topOffset;
        this.maximum = maximum;
        this.tries = tries;
    }
}
