package mcjty.rftoolsbase.modules.hud.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.lib.client.HudRenderHelper;
import mcjty.rftoolsbase.api.client.IHudSupport;
import mcjty.rftoolsbase.modules.hud.network.PacketGetHudLog;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.Direction;

import java.util.List;

public class HudRenderer {

    public static void renderHud(MatrixStack matrixStack, IRenderTypeBuffer buffer, IHudSupport hudSupport, double x, double y, double z) {
        renderHud(matrixStack, buffer, hudSupport, x, y, z, 0.0f, false);
    }

    public static void renderHud(MatrixStack matrixStack, IRenderTypeBuffer buffer, IHudSupport support, double x, double y, double z, float scale, boolean faceVert) {
        List<String> log = support.getClientLog();
        long t = System.currentTimeMillis();
        if (t - support.getLastUpdateTime() > 250) {
            RFToolsBaseMessages.INSTANCE.sendToServer(new PacketGetHudLog(support.getBlockPos()));
            support.setLastUpdateTime(t);
        }
        Direction orientation = support.getBlockOrientation();
        HudRenderHelper.HudPlacement hudPlacement = support.isBlockAboveAir() ? HudRenderHelper.HudPlacement.HUD_ABOVE : HudRenderHelper.HudPlacement.HUD_ABOVE_FRONT;
        HudRenderHelper.HudOrientation hudOrientation = orientation == null ? HudRenderHelper.HudOrientation.HUD_TOPLAYER_HORIZ : HudRenderHelper.HudOrientation.HUD_SOUTH;
        HudRenderHelper.renderHud(matrixStack, buffer, log, hudPlacement, hudOrientation, orientation, x, y, z, 1.0f + scale);
    }
}
