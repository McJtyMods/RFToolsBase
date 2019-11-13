package mcjty.rftoolsbase.modules.infuser;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.blocks.BaseBlockItem;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.container.GenericContainer;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

public class MachineInfuserSetup {

    public static final String INFUSER_REGNAME = "machine_infuser";

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static BaseBlock MACHINE_INFUSER;

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static ContainerType<GenericContainer> CONTAINER_INFUSER;

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static TileEntityType<?> TYPE_INFUSER;


    public static BaseBlock createInfuserBlock() {

        return new BaseBlock(INFUSER_REGNAME, new BlockBuilder()
                .tileEntitySupplier(MachineInfuserTileEntity::new)
                .infusable()
                .info("message.rftoolsbase.shiftmessage")
                .infoExtended("message.rftoolsbase.infuser"));
    }

    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(MachineInfuserSetup.createInfuserBlock());
    }

    public static void registerItems(final RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(RFToolsBase.setup.getTab());
        event.getRegistry().register(new BaseBlockItem(MachineInfuserSetup.MACHINE_INFUSER, properties));
    }

    public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(MachineInfuserTileEntity::new, MachineInfuserSetup.MACHINE_INFUSER).build(null).setRegistryName(MachineInfuserSetup.INFUSER_REGNAME));
    }

    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(GenericContainer.createContainerType(MachineInfuserSetup.INFUSER_REGNAME));
    }

}
