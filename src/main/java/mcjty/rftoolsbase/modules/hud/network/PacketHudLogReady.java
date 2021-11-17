package mcjty.rftoolsbase.modules.hud.network;

import mcjty.lib.network.NetworkTools;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketHudLogReady {

    private BlockPos pos;
    private List<String> list;
    private String command;

    public PacketHudLogReady() {
    }

    public PacketHudLogReady(PacketBuffer buf) {
        pos = buf.readBlockPos();
        command = buf.readUtf(32767);
        list = NetworkTools.readStringList(buf);
    }

    public PacketHudLogReady(BlockPos pos, String command, List<String> list) {
        this.pos = pos;
        this.command = command;
        this.list = new ArrayList<>();
        this.list.addAll(list);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(command);
        NetworkTools.writeStringList(buf, list);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            GenericTileEntity.executeClientCommandHelper(pos, command, list);
        });
        ctx.setPacketHandled(true);
    }
}
