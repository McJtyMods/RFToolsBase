package mcjty.rftoolsbase.modules.infuser;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.container.GenericContainer;
import mcjty.lib.modules.IModule;
import mcjty.rftoolsbase.modules.infuser.blocks.MachineInfuserTileEntity;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.setup.Config;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mcjty.rftoolsbase.setup.Registration.*;

public class MachineInfuserModule implements IModule {

    public static final RegistryObject<BaseBlock> MACHINE_INFUSER = BLOCKS.register("machine_infuser", MachineInfuserTileEntity::createBlock);
    public static final RegistryObject<Item> MACHINE_INFUSER_ITEM = ITEMS.register("machine_infuser", () -> new BlockItem(MACHINE_INFUSER.get(), Registration.createStandardProperties()));
    public static final RegistryObject<TileEntityType<?>> TYPE_MACHINE_INFUSER = TILES.register("machine_infuser", () -> TileEntityType.Builder.create(MachineInfuserTileEntity::new, MACHINE_INFUSER.get()).build(null));
    public static final RegistryObject<ContainerType<GenericContainer>> CONTAINER_MACHINE_INFUSER = CONTAINERS.register("machine_infuser", GenericContainer::createContainerType);

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        GuiMachineInfuser.register();
    }

    @Override
    public void initConfig() {
        MachineInfuserConfiguration.init(Config.SERVER_BUILDER);
    }
}
