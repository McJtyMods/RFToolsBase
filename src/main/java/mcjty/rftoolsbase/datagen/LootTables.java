package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseLootTableProvider;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import mcjty.rftoolsbase.modules.worldgen.WorldGenSetup;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(MachineInfuserSetup.MACHINE_INFUSER.get(), createStandardTable("infuser", MachineInfuserSetup.MACHINE_INFUSER.get()));
        lootTables.put(WorldGenSetup.DIMENSIONAL_SHARD_OVERWORLD.get(), createSilkTouchTable("dimshard", WorldGenSetup.DIMENSIONAL_SHARD_OVERWORLD.get(), VariousSetup.DIMENSIONALSHARD.get(), 4.0f, 5.0f));
        lootTables.put(WorldGenSetup.DIMENSIONAL_SHARD_END.get(), createSilkTouchTable("dimshard", WorldGenSetup.DIMENSIONAL_SHARD_END.get(), VariousSetup.DIMENSIONALSHARD.get(), 4.0f, 5.0f));
        lootTables.put(WorldGenSetup.DIMENSIONAL_SHARD_NETHER.get(), createSilkTouchTable("dimshard", WorldGenSetup.DIMENSIONAL_SHARD_NETHER.get(), VariousSetup.DIMENSIONALSHARD.get(), 4.0f, 5.0f));
        lootTables.put(InformationScreenSetup.INFORMATION_SCREEN.get(), createSimpleTable("informationscreen", InformationScreenSetup.INFORMATION_SCREEN.get()));
    }

    @Override
    public String getName() {
        return "RFToolsBase LootTables";
    }
}
