package mcjty.rftoolsbase.setup;

import mcjty.rftoolsbase.modules.informationscreen.network.PacketMonitorLogReady;

public class ClientMessages {
    public static void register() {
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(PacketMonitorLogReady.TYPE, (packet, context) -> packet.handle(new mcjty.lib.network.PayloadContext(context.player(), context.client())));
    }
}
