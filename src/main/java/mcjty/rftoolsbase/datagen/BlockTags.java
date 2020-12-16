package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        getBuilder(Tags.Blocks.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get())
                .build(new ResourceLocation("minecraft", "ores"));
    }

    @Override
    public String getName() {
        return "RFToolsBase Tags";
    }
}
