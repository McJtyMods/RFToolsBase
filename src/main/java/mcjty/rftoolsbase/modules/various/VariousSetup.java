package mcjty.rftoolsbase.modules.various;

import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.various.items.ManualItem;
import mcjty.rftoolsbase.modules.various.items.SmartWrenchItem;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.ITEMS;

public class VariousSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<SmartWrenchItem> SMARTWRENCH = ITEMS.register("smartwrench", () -> new SmartWrenchItem(SmartWrenchMode.MODE_WRENCH));
    public static final RegistryObject<SmartWrenchItem> SMARTWRENCH_SELECT = ITEMS.register("smartwrench_select", () -> new SmartWrenchItem(SmartWrenchMode.MODE_SELECT));

    public static final RegistryObject<Item> DIMENSIONALSHARD = ITEMS.register("dimensionalshard", VariousSetup::createDimensionalShard);
    public static final RegistryObject<Item> INFUSED_DIAMOND = ITEMS.register("infused_diamond", VariousSetup::createItem16);
    public static final RegistryObject<Item> INFUSED_ENDERPEARL = ITEMS.register("infused_enderpearl", VariousSetup::createItem16);

    public static final RegistryObject<Item> MACHINE_FRAME = ITEMS.register("machine_frame", () -> new Item(Registration.createStandardProperties()));
    public static final RegistryObject<Item> MACHINE_BASE = ITEMS.register("machine_base", () -> new Item(Registration.createStandardProperties()));

    public static final RegistryObject<ManualItem> MANUAL = ITEMS.register("manual", ManualItem::new);

    private static Item createItem16() {
        return new Item(new Item.Properties().group(RFToolsBase.setup.getTab()).maxStackSize(16));
    }

    private static Item createDimensionalShard() {
        return new Item(new Item.Properties()
                .maxStackSize(64)
                .group(RFToolsBase.setup.getTab()));
    }
}
