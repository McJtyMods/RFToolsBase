package mcjty.rftoolsbase.modules.informationscreen;

import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenBlock;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.*;

public class InformationScreenSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<Block> INFORMATION_SCREEN = BLOCKS.register("information_screen", InformationScreenBlock::new);
    public static final RegistryObject<Item> INFORMATION_SCREEN_ITEM = ITEMS.register("information_screen", () -> new BlockItem(INFORMATION_SCREEN.get(), Registration.createStandardProperties()));
    public static final RegistryObject<TileEntityType<InformationScreenTileEntity>> TYPE_INFORMATION_SCREEN = TILES.register("information_screen", () -> TileEntityType.Builder.create(InformationScreenTileEntity::new, INFORMATION_SCREEN.get()).build(null));
}
