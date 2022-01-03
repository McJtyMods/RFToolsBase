package mcjty.rftoolsbase.datagen;

import mcjty.lib.datagen.BaseLootTableProvider;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolsbase.modules.worldgen.WorldGenModule;
import net.minecraft.data.DataGenerator;

import javax.annotation.Nonnull;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(MachineInfuserModule.MACHINE_INFUSER.get(), createStandardTable("infuser", MachineInfuserModule.MACHINE_INFUSER.get(), MachineInfuserModule.TYPE_MACHINE_INFUSER.get()));
        lootTables.put(WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get(), createSilkTouchTable("dimshard", WorldGenModule.DIMENSIONAL_SHARD_OVERWORLD.get(), VariousModule.DIMENSIONALSHARD.get(), 4.0f, 5.0f));
        lootTables.put(WorldGenModule.DIMENSIONAL_SHARD_END.get(), createSilkTouchTable("dimshard", WorldGenModule.DIMENSIONAL_SHARD_END.get(), VariousModule.DIMENSIONALSHARD.get(), 4.0f, 5.0f));
        lootTables.put(WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), createSilkTouchTable("dimshard", WorldGenModule.DIMENSIONAL_SHARD_NETHER.get(), VariousModule.DIMENSIONALSHARD.get(), 4.0f, 5.0f));
        lootTables.put(InformationScreenModule.INFORMATION_SCREEN.get(), createSimpleTable("informationscreen", InformationScreenModule.INFORMATION_SCREEN.get()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "RFToolsBase LootTables";
    }
}
