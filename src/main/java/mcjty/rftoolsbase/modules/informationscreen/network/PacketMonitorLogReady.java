package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public record PacketMonitorLogReady(BlockPos pos, TypedMap data) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "monitorlogready");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        if (data != null) {
            buf.writeBoolean(true);
            TypedMapTools.writeArguments(buf, data);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static PacketMonitorLogReady create(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        TypedMap data;
        if (buf.readBoolean()) {
            data = TypedMapTools.readArguments(buf);
        } else {
            data = null;
        }
        return new PacketMonitorLogReady(pos, data);
    }

    public static PacketMonitorLogReady create(BlockPos pos, TypedMap data) {
        return new PacketMonitorLogReady(pos, data);
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> {
            BlockEntity te = SafeClientTools.getClientWorld().getBlockEntity(pos);
            if (te instanceof InformationScreenTileEntity info) {
                info.setClientData(data);
            }
        });
    }
}
