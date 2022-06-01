package mcjty.rftoolsbase.modules.various;

import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.ManualItem;
import mcjty.rftoolsbase.modules.various.items.SmartWrenchItem;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class VariousModule implements IModule {

    public static final RegistryObject<SmartWrenchItem> SMARTWRENCH = ITEMS.register("smartwrench", () -> new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH));
    public static final RegistryObject<SmartWrenchItem> SMARTWRENCH_SELECT = ITEMS.register("smartwrench_select", () -> new SmartWrenchItem(SmartWrenchMode.MODE_SELECT));

    public static final RegistryObject<Item> DIMENSIONALSHARD = ITEMS.register("dimensionalshard", VariousModule::createDimensionalShard);
    public static final RegistryObject<Item> INFUSED_DIAMOND = ITEMS.register("infused_diamond", VariousModule::createItem16);
    public static final RegistryObject<Item> INFUSED_ENDERPEARL = ITEMS.register("infused_enderpearl", VariousModule::createItem16);

    public static final RegistryObject<Item> MACHINE_FRAME = ITEMS.register("machine_frame", () -> new Item(Registration.createStandardProperties()));
    public static final RegistryObject<Item> MACHINE_BASE = ITEMS.register("machine_base", () -> new Item(Registration.createStandardProperties()));

    public static final RegistryObject<ManualItem> MANUAL = ITEMS.register("manual", ManualItem::new);

    public static final ResourceLocation SHARDS = new ResourceLocation(RFToolsBase.MODID, "shards");
    public static ITag.INamedTag<Item> SHARDS_TAG = ItemTags.bind(VariousModule.SHARDS.toString());

    private static Item createItem16() {
        return new Item(new Item.Properties().tab(RFToolsBase.setup.getTab()).stacksTo(16));
    }

    private static Item createDimensionalShard() {
        return new Item(new Item.Properties()
                .stacksTo(64)
                .tab(RFToolsBase.setup.getTab()));
    }

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {

    }

    @Override
    public void initConfig() {

    }
}
