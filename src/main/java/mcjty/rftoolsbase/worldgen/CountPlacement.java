package mcjty.rftoolsbase.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.SimplePlacement;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.stream.Stream;

public class CountPlacement extends SimplePlacement<CountPlacementConfig> {
    public CountPlacement(Codec<CountPlacementConfig> codec) {
        super(codec);
    }

    @Override
    @Nonnull
    public Stream<BlockPos> place(@Nonnull Random random, CountPlacementConfig config, @Nonnull BlockPos pos) {
        Stream.Builder<BlockPos> builder = Stream.builder();
        for (int i = 0 ; i < config.tries ; i++) {
            int x = pos.getX();
            int z = pos.getZ();
            int y = random.nextInt(config.maximum - config.topOffset) + config.bottomOffset;
            builder.accept(new BlockPos(x, y, z));
        }
        return builder.build();
    }
}
