package mcjty.rftoolsbase.modules.hud.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.McJtyLib;
import mcjty.lib.client.HudRenderHelper;
import mcjty.lib.network.PacketGetListFromServer;
import mcjty.rftoolsbase.api.client.IHudSupport;
import mcjty.rftoolsbase.modules.hud.Hud;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;

import java.util.List;

public class HudRenderer {

    public static void renderHud(PoseStack matrixStack, MultiBufferSource buffer, IHudSupport hudSupport) {
        renderHud(matrixStack, buffer, hudSupport, 0.0f, false);
    }

    public static void renderHud(PoseStack matrixStack, MultiBufferSource buffer, IHudSupport support, float scale, boolean faceVert) {
        List<String> log = support.getClientLog();
        long t = System.currentTimeMillis();
        if (t - support.getLastUpdateTime() > 250) {
            McJtyLib.sendToServer(PacketGetListFromServer.create(support.getHudPos(), Hud.COMMAND_GETHUDLOG));
            support.setLastUpdateTime(t);
        }
        Direction orientation = support.getBlockOrientation();
        HudRenderHelper.HudPlacement hudPlacement = support.isBlockAboveAir() ? HudRenderHelper.HudPlacement.HUD_ABOVE : HudRenderHelper.HudPlacement.HUD_ABOVE_FRONT;
        HudRenderHelper.HudOrientation hudOrientation = orientation == null ? HudRenderHelper.HudOrientation.HUD_TOPLAYER_HORIZ : HudRenderHelper.HudOrientation.HUD_SOUTH;
        HudRenderHelper.renderHud(matrixStack, buffer, log, hudPlacement, hudOrientation, orientation, 0, 0, 0, 1.0f + scale);
    }
}
