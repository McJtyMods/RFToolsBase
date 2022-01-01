package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new LootTables(generator));
            generator.addProvider(new ItemTags(generator, event.getExistingFileHelper()));
            generator.addProvider(new BlockTags(generator, event.getExistingFileHelper()));
        }
    }
}
