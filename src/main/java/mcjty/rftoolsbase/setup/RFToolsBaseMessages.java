package mcjty.rftoolsbase.setup;

import mcjty.lib.network.Networking;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.compat.jei.PacketSendRecipe;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemComponentsToServer;
import mcjty.rftoolsbase.modules.crafting.network.PacketUpdateNBTItemCard;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.filter.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketMonitorLogReady;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.annotation.Nonnull;

public class RFToolsBaseMessages {

    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(RFToolsBase.MODID)
                .versioned("1.0")
                .optional();

        registrar.playToServer(PacketItemComponentsToServer.TYPE, PacketItemComponentsToServer.CODEC, PacketItemComponentsToServer::handle);
        registrar.playToServer(PacketUpdateNBTItemCard.TYPE, PacketUpdateNBTItemCard.CODEC, PacketUpdateNBTItemCard::handle);
        registrar.playToServer(PacketSendRecipe.TYPE, PacketSendRecipe.CODEC, PacketSendRecipe::handle);
        registrar.playToServer(PacketGetMonitorLog.TYPE, PacketGetMonitorLog.CODEC, PacketGetMonitorLog::handle);
        registrar.playToServer(PacketUpdateNBTItemFilter.TYPE, PacketUpdateNBTItemFilter.CODEC, PacketUpdateNBTItemFilter::handle);
        registrar.playToServer(PacketSyncHandItem.TYPE, PacketSyncHandItem.CODEC, PacketSyncHandItem::handle);

        registrar.playToClient(PacketMonitorLogReady.TYPE, PacketMonitorLogReady.CODEC, PacketMonitorLogReady::handle);
    }

    public static void sendToServer(String command, @Nonnull TypedMap.Builder argumentBuilder) {
        Networking.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, argumentBuilder.build()));
    }

    public static void sendToServer(String command) {
        Networking.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, TypedMap.EMPTY));
    }

    public static <T extends CustomPacketPayload> void sendToPlayer(T packet, Player player) {
        PacketDistributor.sendToPlayer((ServerPlayer)player, packet);
    }

    public static <T extends CustomPacketPayload> void sendToServer(T packet) {
        PacketDistributor.sendToServer(packet);
    }
}
