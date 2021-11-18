package mcjty.rftoolsbase.modules.hud.network;

import mcjty.lib.network.AbstractPacketGetListFromServer;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.List;

public class PacketGetHudLog extends AbstractPacketGetListFromServer<String> {

    public static final String COMMAND_GETHUDLOG = "getHudLog";

    public PacketGetHudLog(PacketBuffer buf) {
        super(buf);
    }

    public PacketGetHudLog(BlockPos pos) {
        super(pos, COMMAND_GETHUDLOG, TypedMap.EMPTY);
    }

    @Override
    protected SimpleChannel getChannel() {
        return RFToolsBaseMessages.INSTANCE;
    }

    @Override
    protected Class<String> getType() {
        return String.class;
    }

    @Override
    protected Object createReturnPacket(List<String> list) {
        return new PacketHudLogReady(pos, command, list);
    }
}
