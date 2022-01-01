package mcjty.rftoolsbase.setup;

import mcjty.lib.network.*;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.compat.jei.PacketSendRecipe;
import mcjty.rftoolsbase.modules.crafting.network.PacketItemNBTToServer;
import mcjty.rftoolsbase.modules.crafting.network.PacketUpdateNBTItemCard;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketMonitorLogReady;
import mcjty.rftoolsbase.modules.filter.network.PacketSyncHandItem;
import mcjty.rftoolsbase.modules.filter.network.PacketUpdateNBTItemFilter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;

public class RFToolsBaseMessages {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void registerMessages(String name) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(RFToolsBase.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.registerMessage(id(), PacketItemNBTToServer.class, PacketItemNBTToServer::toBytes, PacketItemNBTToServer::new, PacketItemNBTToServer::handle);
        net.registerMessage(id(), PacketUpdateNBTItemCard.class, PacketUpdateNBTItemCard::toBytes, PacketUpdateNBTItemCard::new, PacketUpdateNBTItemCard::handle);
        net.registerMessage(id(), PacketSendRecipe.class, PacketSendRecipe::toBytes, PacketSendRecipe::new, PacketSendRecipe::handle);
        net.registerMessage(id(), PacketGetMonitorLog.class, PacketGetMonitorLog::toBytes, PacketGetMonitorLog::new, PacketGetMonitorLog::handle);
        net.registerMessage(id(), PacketMonitorLogReady.class, PacketMonitorLogReady::toBytes, PacketMonitorLogReady::new, PacketMonitorLogReady::handle);
        net.registerMessage(id(), PacketUpdateNBTItemFilter.class, PacketUpdateNBTItemFilter::toBytes, PacketUpdateNBTItemFilter::new, PacketUpdateNBTItemFilter::handle);
        net.registerMessage(id(), PacketSyncHandItem.class, PacketSyncHandItem::toBytes, PacketSyncHandItem::new, PacketSyncHandItem::handle);
        net.registerMessage(id(), PacketRequestDataFromServer.class, PacketRequestDataFromServer::toBytes, PacketRequestDataFromServer::new, new ChannelBoundHandler<>(net, PacketRequestDataFromServer::handle));

        PacketHandler.registerStandardMessages(id(), net);
    }

    public static void sendToServer(String command, @Nonnull TypedMap.Builder argumentBuilder) {
        INSTANCE.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, argumentBuilder.build()));
    }

    public static void sendToServer(String command) {
        INSTANCE.sendToServer(new PacketSendServerCommand(RFToolsBase.MODID, command, TypedMap.EMPTY));
    }

    public static void sendToClient(Player player, String command, @Nonnull TypedMap.Builder argumentBuilder) {
        INSTANCE.sendTo(new PacketSendClientCommand(RFToolsBase.MODID, command, argumentBuilder.build()), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToClient(Player player, String command) {
        INSTANCE.sendTo(new PacketSendClientCommand(RFToolsBase.MODID, command, TypedMap.EMPTY), ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
