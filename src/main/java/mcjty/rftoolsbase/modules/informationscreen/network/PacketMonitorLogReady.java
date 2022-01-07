package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMonitorLogReady {

    private final BlockPos pos;
    private final TypedMap data;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        if (data != null) {
            buf.writeBoolean(true);
            TypedMapTools.writeArguments(buf, data);
        } else {
            buf.writeBoolean(false);
        }
    }

    public PacketMonitorLogReady(FriendlyByteBuf buf) {
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
            BlockEntity te = SafeClientTools.getClientWorld().getBlockEntity(pos);
            if (te instanceof InformationScreenTileEntity info) {
                info.setClientData(data);
            }
        });
        ctx.setPacketHandled(true);
    }
}
