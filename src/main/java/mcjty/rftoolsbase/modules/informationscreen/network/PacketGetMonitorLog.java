package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketGetMonitorLog(BlockPos pos) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "getmonitorlog");
    public static final CustomPacketPayload.Type<PacketGetMonitorLog> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, PacketGetMonitorLog> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PacketGetMonitorLog::pos,
            PacketGetMonitorLog::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketGetMonitorLog create(FriendlyByteBuf buf) {
        return new PacketGetMonitorLog(buf.readBlockPos());
    }

    public static PacketGetMonitorLog create(BlockPos pos) {
        return new PacketGetMonitorLog(pos);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            Level world = player.getCommandSenderWorld();
            if (world.hasChunkAt(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof InformationScreenTileEntity infoScreen) {
                    IInformationScreenInfo h = infoScreen.getInfo();
                    if (h != null) {
                        PacketMonitorLogReady packet = new PacketMonitorLogReady(pos, h.getInfo(infoScreen.getMode()));
                        RFToolsBaseMessages.sendToPlayer(packet, player);
                    }
                }
            }
        });
    }
}
