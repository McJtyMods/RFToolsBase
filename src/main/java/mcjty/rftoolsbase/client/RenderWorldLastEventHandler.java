package mcjty.rftoolsbase.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcjty.lib.client.CustomRenderTypes;
import mcjty.rftoolsbase.RFToolsBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class RenderWorldLastEventHandler {

    private static long lastTime = 0;

    public static void tick(RenderWorldLastEvent evt) {
        renderHilightedBlock(evt);
    }

    private static void renderHilightedBlock(RenderWorldLastEvent evt) {
        BlockPos c = RFToolsBase.instance.clientInfo.getHilightedBlock();
        if (c == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        long time = System.currentTimeMillis();

        if (time > RFToolsBase.instance.clientInfo.getExpireHilight()) {
            RFToolsBase.instance.clientInfo.hilightBlock(null, -1);
            return;
        }

        if (((time / 500) & 1) == 0) {
            return;
        }

        MatrixStack matrixStack = evt.getMatrixStack();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        IVertexBuilder builder = buffer.getBuffer(CustomRenderTypes.OVERLAY_LINES);

        matrixStack.pushPose();

        Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        Matrix4f positionMatrix = matrixStack.last().pose();
        mcjty.lib.client.RenderHelper.renderHighLightedBlocksOutline(builder, positionMatrix, c.getX(), c.getY(), c.getZ(), 1.0f, 0.0f, 0.0f, 1.0f);

        matrixStack.popPose();
        RenderSystem.disableDepthTest();
        buffer.endBatch(CustomRenderTypes.OVERLAY_LINES);
    }
}
