package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, new ForgeBlockTagsProvider(generator, helper), RFToolsBase.MODID, helper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(Tags.Items.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END_ITEM.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER_ITEM.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD_ITEM.get());
        getOrCreateBuilder(MachineInfuserModule.MACHINE_INFUSER_CATALYSTS)
                .add(VariousModule.DIMENSIONALSHARD.get());
    }

    @Override
    public String getName() {
        return "RFToolsBase Tags";
    }
}
