package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, RFToolsBase.MODID, helper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(Tags.Blocks.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get());
    }

    @Override
    public String getName() {
        return "RFToolsBase Tags";
    }
}
