package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator generator, BlockTagsProvider blockTagsProvider, ExistingFileHelper helper) {
        super(generator, blockTagsProvider, RFToolsBase.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Items.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END_ITEM.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER_ITEM.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD_ITEM.get());
        tag(Tags.Items.DUSTS)
                .add(VariousModule.DIMENSIONALSHARD.get());
        tag(VariousModule.SHARDS_TAG)
                .add(VariousModule.DIMENSIONALSHARD.get());
    }

    @Nonnull
    @Override
    public String getName() {
        return "RFToolsBase Tags";
    }
}
