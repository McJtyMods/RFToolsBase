package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, RFToolsBase.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Blocks.ORES)
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get(), WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get());
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get())
                .add(WorldGenModule.DIMENSIONAL_SHARD_NETHER.get())
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get())
                .add(InformationScreenModule.INFORMATION_SCREEN.get())
                .add(MachineInfuserModule.MACHINE_INFUSER.get())
                ;
        tag(net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL)
                .add(WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get())
                .add(WorldGenModule.DIMENSIONAL_SHARD_NETHER.get())
                .add(WorldGenModule.DIMENSIONAL_SHARD_END.get())
                .add(InformationScreenModule.INFORMATION_SCREEN.get())
                .add(MachineInfuserModule.MACHINE_INFUSER.get())
        ;
    }

    @Override
    @Nonnull
    public String getName() {
        return "RFToolsBase Tags";
    }
}
