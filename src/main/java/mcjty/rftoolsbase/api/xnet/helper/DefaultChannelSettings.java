package mcjty.rftoolsbase.api.xnet.helper;

import mcjty.rftoolsbase.api.xnet.channels.IControllerContext;
import mcjty.rftoolsbase.api.xnet.channels.RSMode;
import mcjty.rftoolsbase.api.xnet.tiles.IConnectorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class DefaultChannelSettings {

    @Deprecated
    protected boolean checkRedstone(Level world, AbstractConnectorSettings settings, BlockPos extractorPos) {
        RSMode rsMode = settings.getRsMode();
        if (rsMode != RSMode.IGNORED) {
            IConnectorTile connector = (IConnectorTile) world.getBlockEntity(extractorPos);
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

    /**
     * Method to check redstone conditions
     * @return true if connector should work, false otherwise
     */
    protected boolean checkRedstone(AbstractConnectorSettings settings, IConnectorTile connector, IControllerContext context) {
        RSMode rsMode = settings.getRsMode();
        boolean redstoneCondition;
        switch (rsMode) {
            case IGNORED -> {redstoneCondition = true;}
            case OFF -> redstoneCondition = connector.getPowerLevel() == 0;
            case PULSE -> {
                int prevPulse = settings.getPrevPulse();
                settings.setPrevPulse(connector.getPulseCounter());
                redstoneCondition = prevPulse != connector.getPulseCounter();
            }
            default -> redstoneCondition = connector.getPowerLevel() != 0;
        }

        return context.matchColor(settings.getColorsMask()) && redstoneCondition;
    }

}
