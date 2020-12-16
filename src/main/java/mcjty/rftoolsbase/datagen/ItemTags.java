package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        getBuilder(Tags.Items.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END_ITEM.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER_ITEM.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD_ITEM.get())
                .build(new ResourceLocation("minecraft", "ores"));
    }

    @Override
    public String getName() {
        return "RFToolsBase Tags";
    }
}
