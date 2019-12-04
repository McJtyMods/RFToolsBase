package mcjty.rftoolsbase.modules.infuser;

import mcjty.lib.container.GenericContainer;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.rftoolsbase.RFToolsBase.MODID;

public class MachineInfuserSetup {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> MACHINE_INFUSER = BLOCKS.register("machine_infuser", MachineInfuserTileEntity::createBlock);
    public static final RegistryObject<Item> MACHINE_INFUSER_ITEM = ITEMS.register("machine_infuser", () -> new BlockItem(MACHINE_INFUSER.get(), RFToolsBase.createStandardProperties()));
    public static final RegistryObject<TileEntityType<?>> TYPE_MACHINE_INFUSER = TILES.register("machine_infuser", () -> TileEntityType.Builder.create(MachineInfuserTileEntity::new, MACHINE_INFUSER.get()).build(null));
    public static final RegistryObject<ContainerType<GenericContainer>> CONTAINER_MACHINE_INFUSER = CONTAINERS.register("machine_infuser", GenericContainer::createContainerType);
}
