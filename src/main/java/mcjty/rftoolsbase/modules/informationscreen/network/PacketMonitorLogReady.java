package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMonitorLogReady {

    private BlockPos pos;
    private TypedMap data;

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        if (data != null) {
            buf.writeBoolean(true);
            TypedMapTools.writeArguments(buf, data);
        } else {
            buf.writeBoolean(false);
        }
    }

    public PacketMonitorLogReady(PacketBuffer buf) {
        pos = buf.readBlockPos();
        if (buf.readBoolean()) {
            data = TypedMapTools.readArguments(buf);
        } else {
            data = null;
        }
    }

    public PacketMonitorLogReady(BlockPos pos, TypedMap data) {
        this.pos = pos;
        this.data = data;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            TileEntity te = SafeClientTools.getClientWorld().getBlockEntity(pos);
            if (te instanceof InformationScreenTileEntity) {
                InformationScreenTileEntity info = (InformationScreenTileEntity) te;
                info.setClientData(data);
            }
        });
        ctx.setPacketHandled(true);
    }
}
