package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseLootTableProvider;
import mcjty.rftoolsbase.blocks.ModBlocks;
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
        lootTables.put(ModBlocks.DIMENSIONAL_SHARD_OVERWORLD, createSilkTouchTable("dimshard", ModBlocks.DIMENSIONAL_SHARD_OVERWORLD, ModItems.DIMENSIONALSHARD, 4.0f, 5.0f));
        lootTables.put(ModBlocks.DIMENSIONAL_SHARD_END, createSilkTouchTable("dimshard", ModBlocks.DIMENSIONAL_SHARD_END, ModItems.DIMENSIONALSHARD, 4.0f, 5.0f));
        lootTables.put(ModBlocks.DIMENSIONAL_SHARD_NETHER, createSilkTouchTable("dimshard", ModBlocks.DIMENSIONAL_SHARD_NETHER, ModItems.DIMENSIONALSHARD, 4.0f, 5.0f));
    }

    @Override
    public String getName() {
        return "RFToolsBase LootTables";
    }
}
