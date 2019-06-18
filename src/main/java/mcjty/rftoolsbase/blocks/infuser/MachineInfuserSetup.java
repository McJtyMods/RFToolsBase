package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.blocks.GenericBlock;
import mcjty.lib.container.GenericContainer;
import mcjty.rftoolsbase.blocks.ModBlocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class MachineInfuserSetup {

    public static final String INFUSER_REGNAME = "machine_infuser";

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static GenericBlock<MachineInfuserTileEntity, GenericContainer> MACHINE_INFUSER;

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static ContainerType<MachineInfuserContainer> MACHINE_INFUSER_CONTAINER;

    public static TileEntityType<?> TYPE_INFUSER;


    public static GenericBlock<MachineInfuserTileEntity, GenericContainer> createInfuserBlock() {
        return ModBlocks.BUILDER_FACTORY.<MachineInfuserTileEntity> builder(INFUSER_REGNAME)
                .tileEntityClass(MachineInfuserTileEntity.class)
                .container(MachineInfuserTileEntity.CONTAINER_FACTORY)
//                .guiId(GuiProxy.GUI_MACHINE_INFUSER)
                .infusable()
                .info("message.rftoolsbase.shiftmessage")
                .infoExtended("message.rftoolsbase.infuser")
                .build();
    }

    public static void initClient() {
//        MACHINE_INFUSER.setGuiFactory(GuiMachineInfuser::new);
    }
}
