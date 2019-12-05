package mcjty.rftoolsbase.modules.informationscreen.client;

import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class InformationScreenRenderer extends TileEntityRenderer<InformationScreenTileEntity> {

    @Override
    public void render(InformationScreenTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        super.render(te, x, y, z, partialTicks, destroyStage);
        long t = System.currentTimeMillis();
        if (t - te.getLastUpdateTime() > 250) {
            RFToolsBaseMessages.INSTANCE.sendToServer(new PacketGetMonitorLog(te.getPos()));
            te.setLastUpdateTime(t);
        }
        Direction orientation = te.getBlockOrientation();
        if (orientation == null) {
            return;
        }

        te.getInfo().ifPresent(h -> {
            TypedMap data = te.getClientData();
            h.render(te.getMode(), data, orientation, x, y, z, 0.3f);
        });
    }

    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(InformationScreenTileEntity.class, new InformationScreenRenderer());
    }
}
