package mcjty.rftoolsbase.modules.hud.network;

import mcjty.lib.network.TypedMapTools;
import mcjty.lib.tileentity.GenericTileEntity;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.Logging;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class PacketGetHudLog {

    public static final String COMMAND_GETHUDLOG = "getHudLog";

    protected BlockPos pos;
    protected TypedMap params;

    public PacketGetHudLog() {
    }

    public PacketGetHudLog(PacketBuffer buf) {
        pos = buf.readBlockPos();
        params = TypedMapTools.readArguments(buf);
    }

    public PacketGetHudLog(BlockPos pos) {
        this.pos = pos;
        this.params = TypedMap.EMPTY;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        TypedMapTools.writeArguments(buf, params);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            World world = ctx.getSender().getCommandSenderWorld();
            if (world.hasChunkAt(pos)) {
                TileEntity te = world.getBlockEntity(pos);
                if (te instanceof GenericTileEntity) {
                    List<String > list = ((GenericTileEntity) te).executeServerCommandList(COMMAND_GETHUDLOG, ctx.getSender(), TypedMap.EMPTY, String.class);
                    RFToolsBaseMessages.INSTANCE.sendTo(new PacketHudLogReady(pos, COMMAND_GETHUDLOG, list),
                            ctx.getSender().connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                } else {
                    Logging.log("Command " + COMMAND_GETHUDLOG + " was not handled!");
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
