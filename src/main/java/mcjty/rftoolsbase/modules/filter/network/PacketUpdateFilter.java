package mcjty.rftoolsbase.modules.filter.network;

import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.FilterModule;
import mcjty.rftoolsbase.modules.filter.data.FilterModuleData;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketUpdateFilter(int containerId, FilterModuleData data) implements CustomPacketPayload {

    public static final Type<PacketUpdateFilter> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "update_filter"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketUpdateFilter> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PacketUpdateFilter::containerId,
            FilterModuleData.STREAM_CODEC, PacketUpdateFilter::data,
            PacketUpdateFilter::new
    );

    @Override
    public Type<PacketUpdateFilter> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player.containerMenu instanceof FilterModuleContainer menu
                    && menu.containerId == containerId && menu.stillValid(player)) {
                player.getMainHandItem().set(FilterModule.ITEM_FILTERMODULE_DATA, data);
            }
        });
    }
}
