package mcjty.rftoolsbase.setup;


import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.crafting.CraftingSetup;
import mcjty.rftoolsbase.modules.filter.FilterSetup;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenSetup;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserSetup;
import mcjty.rftoolsbase.modules.tablet.TabletSetup;
import mcjty.rftoolsbase.modules.various.VariousSetup;
import mcjty.rftoolsbase.modules.worldgen.WorldGenSetup;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.rftoolsbase.RFToolsBase.MODID;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        WorldGenSetup.register();
        MachineInfuserSetup.register();
        InformationScreenSetup.register();
        CraftingSetup.register();
        VariousSetup.register();
        FilterSetup.register();
        TabletSetup.register();
    }


    public static Item.Properties createStandardProperties() {
        return new Item.Properties().group(RFToolsBase.setup.getTab());
    }
}
