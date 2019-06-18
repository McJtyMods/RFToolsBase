package mcjty.rftoolsbase.blocks;

import mcjty.lib.builder.GenericBlockBuilderFactory;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("rftoolsbase:dimensionalshard_overworld")
    public static DimensionalShardBlock DIMENSIONAL_SHARD_OVERWORLD;

    @ObjectHolder("rftoolsbase:dimensionalshard_nether")
    public static DimensionalShardBlock DIMENSIONAL_SHARD_NETHER;

    @ObjectHolder("rftoolsbase:dimensionalshard_end")
    public static DimensionalShardBlock DIMENSIONAL_SHARD_END;

    public static final GenericBlockBuilderFactory BUILDER_FACTORY = new GenericBlockBuilderFactory(RFToolsBase.instance)
            .creativeTabs(RFToolsBase.setup.getTab());;

}
