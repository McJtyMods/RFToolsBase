package mcjty.rftoolsbase.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mcjty.lib.client.CustomRenderTypes;
import mcjty.lib.client.RenderHelper;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;

public class RenderWorldLastEventHandler {

    public static void tick(RenderLevelLastEvent evt) {
        renderHilightedBlock(evt);
    }

    private static void renderHilightedBlock(RenderLevelLastEvent evt) {
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
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(CustomRenderTypes.OVERLAY_LINES);

        matrixStack.pushPose();

        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        RenderHelper.renderHighLightedBlocksOutline(matrixStack, builder, c.getX(), c.getY(), c.getZ(), 1.0f, 0.0f, 0.0f, 1.0f);

        matrixStack.popPose();
        RenderSystem.disableDepthTest();
        buffer.endBatch(CustomRenderTypes.OVERLAY_LINES);
    }
}
