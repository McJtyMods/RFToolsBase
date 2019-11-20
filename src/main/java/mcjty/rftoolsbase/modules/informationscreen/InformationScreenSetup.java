package mcjty.rftoolsbase.modules.informationscreen;

import mcjty.lib.blocks.BaseBlockItem;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenBlock;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;

public class InformationScreenSetup {

    @ObjectHolder(RFToolsBase.MODID + ":" + InformationScreenTileEntity.REGNAME)
    public static InformationScreenBlock INFORMATION_SCREEN;

    @ObjectHolder(RFToolsBase.MODID + ":" + InformationScreenTileEntity.REGNAME)
    public static TileEntityType<?> TYPE_INFORMATION_SCREEN;

    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new InformationScreenBlock());
    }

    public static void registerItems(final RegistryEvent.Register<Item> event) {
        Item.Properties properties = new Item.Properties().group(RFToolsBase.setup.getTab());
        event.getRegistry().register(new BaseBlockItem(INFORMATION_SCREEN, properties));
    }

    public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(TileEntityType.Builder.create(InformationScreenTileEntity::new, INFORMATION_SCREEN).build(null).setRegistryName(INFORMATION_SCREEN.getRegistryName()));
    }

    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
    }
}
