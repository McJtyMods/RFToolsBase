package mcjty.rftoolsbase.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class DimensionalShardBlock extends Block {

    private final OreType oreType;

    public DimensionalShardBlock(OreType variant) {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(3.0f, 5.0f)
                .lightValue(7));
        this.oreType = variant;
        setRegistryName("dimensionalshard_" + oreType.getName());
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    public enum OreType implements IStringSerializable {
        ORE_OVERWORLD("overworld"),
        ORE_NETHER("nether"),
        ORE_END("end");

        private final String name;

        OreType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public void onPlayerDestroy(IWorld iworld, BlockPos pos, BlockState state) {
        World world = iworld.getWorld();
        if (world.isRemote) {
            for (int i = 0 ; i < 10 ; i++) {
                world.addParticle(ParticleTypes.FIREWORK, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, rand.nextGaussian() / 3.0f, rand.nextGaussian() / 3.0f, rand.nextGaussian() / 3.0f);
            }
        }
    }

//    @Override
//    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
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
//    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
//         @todo Check @@@@@@@@@@
//        return rand.nextInt(7-3) + 3;
//    }
}
