package mcjty.rftoolsbase.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mcjty.lib.client.CustomRenderTypes;
import mcjty.lib.client.RenderHelper;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

public class RenderWorldLastEventHandler {

    // @todo 1.20 is this the right event?
    public static void tick(SubmitCustomGeometryEvent evt) {
        renderHilightedBlock(evt);
    }

    private static void renderHilightedBlock(SubmitCustomGeometryEvent evt) {
        BlockPos c = RFToolsBase.instance.clientInfo.getHilightedBlock();
        if (c == null) {
            return;
        }
        long time = System.currentTimeMillis();

        if (time > RFToolsBase.instance.clientInfo.getExpireHilight()) {
            RFToolsBase.instance.clientInfo.hilightBlock(null, -1);
            return;
        }

        if (((time / 500) & 1) == 0) {
            return;
        }

        PoseStack matrixStack = evt.getPoseStack();
        matrixStack.pushPose();

        Vec3 projectedView = Minecraft.getInstance().gameRenderer.mainCamera().position();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        evt.getSubmitNodeCollector().submitCustomGeometry(matrixStack, CustomRenderTypes.OVERLAY_LINES, (pose, builder) -> {
            PoseStack captured = new PoseStack();
            captured.last().pose().set(pose.pose());
            RenderHelper.renderHighLightedBlocksOutline(captured, builder, c.getX(), c.getY(), c.getZ(), 1.0f, 0.0f, 0.0f, 1.0f);
        });

        matrixStack.popPose();
    }
}
