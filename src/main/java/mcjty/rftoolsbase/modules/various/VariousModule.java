package mcjty.rftoolsbase.modules.various;

import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.datagen.DataGen;
import mcjty.lib.datagen.Dob;
import mcjty.lib.modules.IModule;
import mcjty.lib.varia.TagTools;
import mcjty.lib.varia.WrenchChecker;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.ManualItem;
import mcjty.rftoolsbase.modules.various.items.SmartWrenchItem;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

import static mcjty.lib.datagen.DataGen.has;
import static mcjty.rftoolsbase.RFToolsBase.tab;
import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class VariousModule implements IModule {

    public static final DeferredItem<SmartWrenchItem> SMARTWRENCH = ITEMS.register("smartwrench", tab(() -> new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH)));
    public static final DeferredItem<SmartWrenchItem> SMARTWRENCH_SELECT = ITEMS.register("smartwrench_select", tab(() -> new SmartWrenchItem(SmartWrenchMode.MODE_SELECT)));

    public static final DeferredItem<Item> DIMENSIONALSHARD = ITEMS.register("dimensionalshard", tab(VariousModule::createDimensionalShard));
    public static final DeferredItem<Item> INFUSED_DIAMOND = ITEMS.register("infused_diamond", tab(VariousModule::createItem16));
    public static final DeferredItem<Item> INFUSED_ENDERPEARL = ITEMS.register("infused_enderpearl", tab(VariousModule::createItem16));

    public static final DeferredItem<Item> MACHINE_FRAME = ITEMS.register("machine_frame", tab(() -> new Item(Registration.createStandardProperties())));
    public static final DeferredItem<Item> MACHINE_BASE = ITEMS.register("machine_base", tab(() -> new Item(Registration.createStandardProperties())));

    public static final DeferredItem<ManualItem> MANUAL = ITEMS.register("manual", tab(ManualItem::new));

    public static final ResourceLocation SHARDS = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "shards");
    public static final TagKey<Item> SHARDS_TAG = TagTools.createItemTagKey(SHARDS);

    private static Item createItem16() {
        return new Item(RFToolsBase.setup.defaultProperties().stacksTo(16));
    }

    private static Item createDimensionalShard() {
        return new Item(RFToolsBase.setup.defaultProperties().stacksTo(64));
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {

    }

    @Override
    public void initConfig(IEventBus bus) {

    }

    @Override
    public void initDatagen(DataGen dataGen, HolderLookup.Provider provider) {
        dataGen.add(
                Dob.itemBuilder(DIMENSIONALSHARD)
                        .itemTags(List.of(Tags.Items.DUSTS, SHARDS_TAG))
                        .shaped(builder -> builder
                                        .define('q', Items.QUARTZ)
                                        .define('C', Items.PRISMARINE_SHARD)
                                        .define('g', Items.GOLD_INGOT)
                                        .define('X', Items.GLOWSTONE_DUST)
                                        .unlockedBy("gold", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT,
                                                Items.QUARTZ, Items.REDSTONE, Items.PRISMARINE_SHARD, Items.DIAMOND,
                                                Items.EMERALD, Items.GLOWSTONE_DUST, Items.IRON_INGOT, Items.GLASS)),
                                "deg", "irX", "qCG"),
                Dob.itemBuilder(SMARTWRENCH)
                        .itemTags(List.of(WrenchChecker.WRENCH_TAG))
                        .shaped(builder -> builder
                                        .define('l', Tags.Items.DYES_BLUE)
                                        .unlockedBy("iron", has(Items.IRON_INGOT)),
                                "  i", " l ", "l  "),
                Dob.itemBuilder(SMARTWRENCH_SELECT)
                        .itemTags(List.of(WrenchChecker.WRENCH_TAG)),
                Dob.itemBuilder(MACHINE_BASE)
                        .shaped(builder -> builder
                                        .define('g', Items.GOLD_NUGGET)
                                        .define('s', Tags.Items.STONES)
                                        .unlockedBy("gold", has(Items.GOLD_NUGGET)),
                                "ggg", "sss"),
                Dob.itemBuilder(MACHINE_FRAME)
                        .shaped(builder -> builder
                                        .define('g', Items.GOLD_NUGGET)
                                        .define('l', Tags.Items.DYES_BLUE)
                                        .unlockedBy("iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT, Items.GOLD_NUGGET)),
                                "ili", "g g", "ili"),
                Dob.itemBuilder(INFUSED_DIAMOND)
                        .shaped(builder -> builder
                                        .define('s', VariousModule.DIMENSIONALSHARD.get())
                                        .unlockedBy("shard", has(VariousModule.DIMENSIONALSHARD.get())),
                                "sss", "sds", "sss"),
                Dob.itemBuilder(INFUSED_ENDERPEARL)
                        .shaped(builder -> builder
                                        .define('s', VariousModule.DIMENSIONALSHARD.get())
                                        .unlockedBy("shard", has(VariousModule.DIMENSIONALSHARD.get())),
                                "sss", "sos", "sss"),
                Dob.itemBuilder(MANUAL)
                        .shaped(builder -> builder
                                        .define('X', Items.BOOK)
                                        .define('g', Tags.Items.NUGGETS_IRON)
                                        .unlockedBy("book", has(Items.BOOK)),
                                "Xg", "gg")
        );
    }
}
