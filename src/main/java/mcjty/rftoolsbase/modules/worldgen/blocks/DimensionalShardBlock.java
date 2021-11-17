package mcjty.rftoolsbase.modules.worldgen.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class DimensionalShardBlock extends Block {

    public DimensionalShardBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0f, 5.0f)
                .lightLevel(value -> 7));
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(@Nonnull BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public int getHarvestLevel(@Nonnull BlockState state) {
        return 2;
    }

    @Override
    public void destroy(IWorld world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (world.isClientSide()) {
            for (int i = 0 ; i < 10 ; i++) {
                world.addParticle(ParticleTypes.FIREWORK, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, rand.nextGaussian() / 3.0f, rand.nextGaussian() / 3.0f, rand.nextGaussian() / 3.0f);
            }
        }
    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune) {
//        return ModItems.dimensionalShardItem;
//    }
//
//    @Override
//    public int quantityDropped(Random random) {
//        return 2 + random.nextInt(3);
//    }
//
//    @Override
//    public int quantityDroppedWithBonus(int bonus, Random random) {
//        int j = random.nextInt(bonus + 2) - 1;
//        if (j < 0) {
//            j = 0;
//        }
//
//        return this.quantityDropped(random) * (j + 1);
//    }

    private Random rand = new Random();

//    @Override
//    public int getExpDrop(BlockState state, IBlockAccess world, BlockPos pos, int fortune) {
//         @todo Check @@@@@@@@@@
//        return rand.nextInt(7-3) + 3;
//    }
}
