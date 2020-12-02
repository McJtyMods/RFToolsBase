package mcjty.rftoolsbase.api.xnet.helper;

import mcjty.rftoolsbase.api.xnet.channels.RSMode;
import mcjty.rftoolsbase.api.xnet.tiles.IConnectorTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultChannelSettings {

    protected boolean checkRedstone(World world, AbstractConnectorSettings settings, BlockPos extractorPos) {
        RSMode rsMode = settings.getRsMode();
        if (rsMode != RSMode.IGNORED) {
            IConnectorTile connector = (IConnectorTile) world.getTileEntity(extractorPos);
            if (rsMode == RSMode.PULSE) {
                int prevPulse = settings.getPrevPulse();
                settings.setPrevPulse(connector.getPulseCounter());
                if (prevPulse == connector.getPulseCounter()) {
                    return true;
                }
            } else if ((rsMode == RSMode.ON) != (connector.getPowerLevel() > 0)) {
                return true;
            }
        }
        return false;
    }

}
