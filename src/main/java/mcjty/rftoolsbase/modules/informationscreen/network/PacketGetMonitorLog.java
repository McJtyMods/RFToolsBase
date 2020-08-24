package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGetMonitorLog {

    private BlockPos pos;

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    public PacketGetMonitorLog(PacketBuffer buf) {
        pos = buf.readBlockPos();
    }

    public PacketGetMonitorLog(BlockPos pos) {
        this.pos = pos;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            PlayerEntity player = ctx.getSender();
            World world = player.getEntityWorld();
            if (world.isBlockLoaded(pos)) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof InformationScreenTileEntity) {
                    InformationScreenTileEntity infoScreen = (InformationScreenTileEntity) te;
                    infoScreen.getInfo().ifPresent(h -> {
                        PacketMonitorLogReady packet = new PacketMonitorLogReady(pos, h.getInfo(infoScreen.getMode()));
                        RFToolsBaseMessages.INSTANCE.sendTo(packet, ((ServerPlayerEntity) player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                    });
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
