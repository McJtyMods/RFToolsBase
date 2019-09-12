package mcjty.rftoolsbase.blocks.infuser;

import mcjty.lib.blocks.BaseBlock;
import mcjty.lib.builder.BlockBuilder;
import mcjty.lib.container.GenericContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
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
}
