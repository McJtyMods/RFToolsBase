package mcjty.rftoolsbase.setup;

import mcjty.lib.network.PacketHandler;
import mcjty.lib.network.PacketRequestDataFromServer;
import mcjty.lib.network.PacketSendClientCommand;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;

import static mcjty.lib.network.PlayPayloadContext.wrap;

public class RFToolsBaseMessages {
    private static SimpleChannel INSTANCE;

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

        net.registerMessage(id(), PacketItemNBTToServer.class, PacketItemNBTToServer::write, PacketItemNBTToServer::create, wrap(PacketItemNBTToServer::handle));
        net.registerMessage(id(), PacketUpdateNBTItemCard.class, PacketUpdateNBTItemCard::write, PacketUpdateNBTItemCard::create, wrap(PacketUpdateNBTItemCard::handle));
        net.registerMessage(id(), PacketSendRecipe.class, PacketSendRecipe::write, PacketSendRecipe::create, wrap(PacketSendRecipe::handle));
        net.registerMessage(id(), PacketGetMonitorLog.class, PacketGetMonitorLog::write, PacketGetMonitorLog::create, wrap(PacketGetMonitorLog::handle));
        net.registerMessage(id(), PacketMonitorLogReady.class, PacketMonitorLogReady::write, PacketMonitorLogReady::create, wrap(PacketMonitorLogReady::handle));
        net.registerMessage(id(), PacketUpdateNBTItemFilter.class, PacketUpdateNBTItemFilter::write, PacketUpdateNBTItemFilter::create, wrap(PacketUpdateNBTItemFilter::handle));
        net.registerMessage(id(), PacketSyncHandItem.class, PacketSyncHandItem::write, PacketSyncHandItem::create, wrap(PacketSyncHandItem::handle));

        PacketRequestDataFromServer.register(net, id());

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

    public static <T> void sendToPlayer(T packet, Player player) {
        INSTANCE.sendTo(packet, ((ServerPlayer)player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }
}
