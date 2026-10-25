package mcjty.rftoolsbase.setup;

import mcjty.lib.network.Networking;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.compat.jei.PacketSendRecipe;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemComponentsToServer;
import mcjty.rftoolsbase.modules.crafting.network.PacketUpdateNBTItemCard;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketMonitorLogReady;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nonnull;

public class RFToolsBaseMessages {
    public static void registerMessages() {
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(PacketItemComponentsToServer.TYPE, PacketItemComponentsToServer.CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(PacketItemComponentsToServer.TYPE, (packet, context) -> packet.handle(new mcjty.lib.network.PayloadContext(context.player(), context.server())));
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(PacketUpdateNBTItemCard.TYPE, PacketUpdateNBTItemCard.CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(PacketUpdateNBTItemCard.TYPE, (packet, context) -> packet.handle(new mcjty.lib.network.PayloadContext(context.player(), context.server())));
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(PacketSendRecipe.TYPE, PacketSendRecipe.CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(PacketSendRecipe.TYPE, (packet, context) -> packet.handle(new mcjty.lib.network.PayloadContext(context.player(), context.server())));
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(PacketGetMonitorLog.TYPE, PacketGetMonitorLog.CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(PacketGetMonitorLog.TYPE, (packet, context) -> packet.handle(new mcjty.lib.network.PayloadContext(context.player(), context.server())));
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.serverboundPlay().register(PacketSyncHandItem.TYPE, PacketSyncHandItem.CODEC);
        net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerGlobalReceiver(PacketSyncHandItem.TYPE, (packet, context) -> packet.handle(new mcjty.lib.network.PayloadContext(context.player(), context.server())));
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(PacketMonitorLogReady.TYPE, PacketMonitorLogReady.CODEC);
    }

    public static void sendToServer(String command, @Nonnull TypedMap.Builder argumentBuilder) {
        Networking.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, argumentBuilder.build()));
    }

    public static void sendToServer(String command) {
        Networking.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, TypedMap.EMPTY));
    }

    public static <T extends CustomPacketPayload> void sendToPlayer(T packet, Player player) {
        Networking.sendToPlayer(packet, player);
    }

    public static <T extends CustomPacketPayload> void sendToServer(T packet) {
        Networking.sendToServer(packet);
    }
}
