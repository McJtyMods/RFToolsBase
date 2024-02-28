package mcjty.rftoolsbase.setup;

import mcjty.lib.network.IPayloadRegistrar;
import mcjty.lib.network.Networking;
import mcjty.lib.network.PacketSendServerCommand;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.compat.jei.PacketSendRecipe;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemNBTToServer;
import mcjty.rftoolsbase.modules.crafting.network.PacketUpdateNBTItemCard;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.filter.network.PacketUpdateNBTItemFilter;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketMonitorLogReady;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.NetworkDirection;

import javax.annotation.Nonnull;

public class RFToolsBaseMessages {

    private static IPayloadRegistrar registrar;

    public static void registerMessages() {
        registrar = Networking.registrar(RFToolsBase.MODID)
                .versioned("1.0")
                .optional();

        registrar.play(PacketItemNBTToServer.class, PacketItemNBTToServer::create, handler -> handler.server(PacketItemNBTToServer::handle));
        registrar.play(PacketUpdateNBTItemCard.class, PacketUpdateNBTItemCard::create, handler -> handler.server(PacketUpdateNBTItemCard::handle));
        registrar.play(PacketSendRecipe.class, PacketSendRecipe::create, handler -> handler.server(PacketSendRecipe::handle));
        registrar.play(PacketGetMonitorLog.class, PacketGetMonitorLog::create, handler -> handler.server(PacketGetMonitorLog::handle));
        registrar.play(PacketMonitorLogReady.class, PacketMonitorLogReady::create, handler -> handler.client(PacketMonitorLogReady::handle));
        registrar.play(PacketUpdateNBTItemFilter.class, PacketUpdateNBTItemFilter::create, handler -> handler.server(PacketUpdateNBTItemFilter::handle));
        registrar.play(PacketSyncHandItem.class, PacketSyncHandItem::create, handler -> handler.server(PacketSyncHandItem::handle));
    }

    public static void sendToServer(String command, @Nonnull TypedMap.Builder argumentBuilder) {
        Networking.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, argumentBuilder.build()));
    }

    public static void sendToServer(String command) {
        Networking.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, TypedMap.EMPTY));
    }

    public static <T> void sendToPlayer(T packet, Player player) {
        registrar.getChannel().sendTo(packet, ((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T packet) {
        registrar.getChannel().sendToServer(packet);
    }
}
