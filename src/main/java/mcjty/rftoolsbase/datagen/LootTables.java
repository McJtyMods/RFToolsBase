package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseLootTableProvider;
import mcjty.rftoolsbase.modules.worldgen.WorldGenSetup;
import mcjty.rftoolsbase.items.ModItems;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(MachineInfuserSetup.MACHINE_INFUSER, createStandardTable("infuser", MachineInfuserSetup.MACHINE_INFUSER));
        lootTables.put(WorldGenSetup.DIMENSIONAL_SHARD_OVERWORLD, createSilkTouchTable("dimshard", WorldGenSetup.DIMENSIONAL_SHARD_OVERWORLD, ModItems.DIMENSIONALSHARD, 4.0f, 5.0f));
        lootTables.put(WorldGenSetup.DIMENSIONAL_SHARD_END, createSilkTouchTable("dimshard", WorldGenSetup.DIMENSIONAL_SHARD_END, ModItems.DIMENSIONALSHARD, 4.0f, 5.0f));
        lootTables.put(WorldGenSetup.DIMENSIONAL_SHARD_NETHER, createSilkTouchTable("dimshard", WorldGenSetup.DIMENSIONAL_SHARD_NETHER, ModItems.DIMENSIONALSHARD, 4.0f, 5.0f));
    }

    @Override
    public String getName() {
        return "RFToolsBase LootTables";
    }
}
