package mcjty.rftoolsbase.modules.informationscreen.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenSetup;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class InformationScreenRenderer extends TileEntityRenderer<InformationScreenTileEntity> {

    public InformationScreenRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(InformationScreenTileEntity te, float v, MatrixStack matrixStack, IRenderTypeBuffer buffer, int i, int i1) {
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
            h.render(te.getMode(), matrixStack, buffer, data, orientation, 0.3f);
        });
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(InformationScreenSetup.TYPE_INFORMATION_SCREEN.get(), InformationScreenRenderer::new);
    }
}
