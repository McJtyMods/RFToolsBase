package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseBlockTagsProvider;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class BlockTags extends BaseBlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, RFToolsBase.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(WorldGenModule.DIMENSIONAL_SHARD_ORE)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get());
        tag(Tags.Blocks.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get());
        ironPickaxe(
                WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD,
                WorldGenModule.DIMENSIONAL_SHARD_NETHER,
                WorldGenModule.DIMENSIONAL_SHARD_END,
                InformationScreenModule.INFORMATION_SCREEN,
                MachineInfuserModule.MACHINE_INFUSER);
    }

    @Override
    @Nonnull
    public String getName() {
        return "RFToolsBase Tags";
    }
}
