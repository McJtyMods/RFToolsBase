package mcjty.rftoolsbase.modules.hud.network;

import mcjty.lib.network.AbstractPacketSendResultToClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class PacketHudLogReady extends AbstractPacketSendResultToClient<String> {

    public PacketHudLogReady(PacketBuffer buf) {
        super(buf);
    }

    public PacketHudLogReady(BlockPos pos, String command, List<String> list) {
        super(pos, command, list);
    }

    @Override
    protected String readElement(PacketBuffer buf) {
        return buf.readUtf(32767);
    }

    @Override
    protected void writeElement(PacketBuffer buf, String element) {
        buf.writeUtf(element);
    }
}
