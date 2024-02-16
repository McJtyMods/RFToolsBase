package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record PacketGetMonitorLog(BlockPos pos) implements CustomPacketPayload {

    public static ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "getmonitorlog");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketGetMonitorLog create(FriendlyByteBuf buf) {
        return new PacketGetMonitorLog(buf.readBlockPos());
    }

    public static PacketGetMonitorLog create(BlockPos pos) {
        return new PacketGetMonitorLog(pos);
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            ctx.player().ifPresent(player -> {
                Level world = player.getCommandSenderWorld();
                if (world.hasChunkAt(pos)) {
                    BlockEntity te = world.getBlockEntity(pos);
                    if (te instanceof InformationScreenTileEntity infoScreen) {
                        infoScreen.getInfo().ifPresent(h -> {
                            PacketMonitorLogReady packet = new PacketMonitorLogReady(pos, h.getInfo(infoScreen.getMode()));
                            RFToolsBaseMessages.sendToPlayer(packet, player);
                        });
                    }
                }
            });
        });
    }
}
