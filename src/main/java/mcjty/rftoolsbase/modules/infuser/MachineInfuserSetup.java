package mcjty.rftoolsbase.modules.infuser;

import mcjty.lib.container.GenericContainer;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import static mcjty.rftoolsbase.setup.Registration.*;

public class MachineInfuserSetup {

    public static void register() {
        // Needed to force class loading
    }

    public static final RegistryObject<Block> MACHINE_INFUSER = BLOCKS.register("machine_infuser", MachineInfuserTileEntity::createBlock);
    public static final RegistryObject<Item> MACHINE_INFUSER_ITEM = ITEMS.register("machine_infuser", () -> new BlockItem(MACHINE_INFUSER.get(), Registration.createStandardProperties()));
    public static final RegistryObject<TileEntityType<?>> TYPE_MACHINE_INFUSER = TILES.register("machine_infuser", () -> TileEntityType.Builder.create(MachineInfuserTileEntity::new, MACHINE_INFUSER.get()).build(null));
    public static final RegistryObject<ContainerType<GenericContainer>> CONTAINER_MACHINE_INFUSER = CONTAINERS.register("machine_infuser", GenericContainer::createContainerType);
}
