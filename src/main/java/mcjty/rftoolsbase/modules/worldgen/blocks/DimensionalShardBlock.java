package mcjty.rftoolsbase.modules.worldgen.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;
import java.util.Random;

public class DimensionalShardBlock extends Block {

    public DimensionalShardBlock() {
        super(Properties.of()
                .sound(SoundType.METAL)
                .mapColor(MapColor.METAL)
                .strength(3.0f, 5.0f)
                .lightLevel(value -> 7));
    }

    private final Random rand = new Random();

    @Override
    public void destroy(LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (world.isClientSide()) {
            for (int i = 0 ; i < 10 ; i++) {
                world.addParticle(ParticleTypes.FIREWORK, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, rand.nextGaussian() / 3.0f, rand.nextGaussian() / 3.0f, rand.nextGaussian() / 3.0f);
            }
        }
    }
}
