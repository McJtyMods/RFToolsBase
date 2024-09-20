package mcjty.rftoolsbase.modules.informationscreen.network;

import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.SafeClientTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketMonitorLogReady(BlockPos pos, TypedMap data) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "monitorlogready");
    public static final CustomPacketPayload.Type<PacketMonitorLogReady> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketMonitorLogReady> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PacketMonitorLogReady::pos,
            TypedMap.STREAM_CODEC, PacketMonitorLogReady::data, PacketMonitorLogReady::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PacketMonitorLogReady create(BlockPos pos, TypedMap data) {
        return new PacketMonitorLogReady(pos, data);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            BlockEntity te = SafeClientTools.getClientWorld().getBlockEntity(pos);
            if (te instanceof InformationScreenTileEntity info) {
                info.setClientData(data);
            }
        });
    }
}
