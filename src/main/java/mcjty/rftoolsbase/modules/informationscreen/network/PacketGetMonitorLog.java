package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGetMonitorLog {

    private BlockPos pos;

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public PacketGetMonitorLog(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
    }

    public PacketGetMonitorLog(BlockPos pos) {
        this.pos = pos;
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            Level world = player.getCommandSenderWorld();
            if (world.hasChunkAt(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof InformationScreenTileEntity) {
                    InformationScreenTileEntity infoScreen = (InformationScreenTileEntity) te;
                    infoScreen.getInfo().ifPresent(h -> {
                        PacketMonitorLogReady packet = new PacketMonitorLogReady(pos, h.getInfo(infoScreen.getMode()));
                        RFToolsBaseMessages.INSTANCE.sendTo(packet, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                    });
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
