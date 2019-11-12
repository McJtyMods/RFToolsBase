package mcjty.rftoolsbase.items;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

    @ObjectHolder("rftoolsbase:smartwrench")
    public static SmartWrenchItem SMARTWRENCH;

    @ObjectHolder("rftoolsbase:infused_diamond")
    public static Item INFUSED_DIAMOND;

    @ObjectHolder("rftoolsbase:infused_enderpearl")
    public static Item INFUSED_ENDERPEARL;

    @ObjectHolder("rftoolsbase:smartwrench_select")
    public static SmartWrenchItem SMARTWRENCH_SELECT;

    @ObjectHolder("rftoolsbase:dimensionalshard")
    public static Item DIMENSIONALSHARD;


    @ObjectHolder("rftoolsbase:machine_frame")
    public static Item MACHINE_FRAME;

    @ObjectHolder("rftoolsbase:machine_base")
    public static Item MACHINE_BASE;

    @ObjectHolder("rftoolsbase:crafting_card")
    public static ContainerType<CraftingCardContainer> CONTAINER_CRAFTING_CARD;

}
