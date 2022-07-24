package mcjty.rftoolsbase.datagen;

import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RFToolsBase.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new Recipes(generator));
        generator.addProvider(event.includeServer(), new LootTables(generator));
        BlockTags blockTags = new BlockTags(generator, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTags(generator, blockTags, event.getExistingFileHelper()));
    }
}
