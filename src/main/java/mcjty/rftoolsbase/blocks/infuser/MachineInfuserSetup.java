package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.blocks.BaseBlockNew;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.container.GenericContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class MachineInfuserSetup {

    public static final String INFUSER_REGNAME = "machine_infuser";

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static BaseBlockNew MACHINE_INFUSER;

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static ContainerType<GenericContainer> MACHINE_INFUSER_CONTAINER;

    @ObjectHolder("rftoolsbase:machine_infuser")
    public static TileEntityType<?> TYPE_INFUSER;


    public static BaseBlockNew createInfuserBlock() {

        return new BaseBlockNew(INFUSER_REGNAME, new BlockBuilder()
                .tileEntitySupplier(MachineInfuserTileEntity::new)
                .hasGui()
                .infusable()
                .info("message.rftoolsbase.shiftmessage")
                .infoExtended("message.rftoolsbase.infuser"));
    }

    public static void initClient() {
//        MACHINE_INFUSER.setGuiFactory(GuiMachineInfuser::new);
    }
}
