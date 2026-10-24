package mcjty.rftoolsbase.modules.informationscreen.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.api.infoscreen.IInformationScreenInfo;
import mcjty.rftoolsbase.modules.informationscreen.InformationScreenModule;
import mcjty.rftoolsbase.modules.informationscreen.blocks.InformationScreenTileEntity;
import mcjty.rftoolsbase.modules.informationscreen.network.PacketGetMonitorLog;
import mcjty.rftoolsbase.setup.RFToolsBaseMessages;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Direction;

import javax.annotation.Nonnull;

public class InformationScreenRenderer implements BlockEntityRenderer<InformationScreenTileEntity, InformationScreenRenderer.State> {

    public InformationScreenRenderer(BlockEntityRendererProvider.Context dispatcher) {
    }

    public static class State extends net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState {
        IInformationScreenInfo info;
        TypedMap data;
        Direction orientation;
        int mode;
    }

    @Override public State createRenderState() { return new State(); }

    @Override
    public void extractRenderState(InformationScreenTileEntity te, State state, float partialTicks,
            net.minecraft.world.phys.Vec3 camera, net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(te, state, partialTicks, camera, overlay);
        long time = System.currentTimeMillis();
        if (time - te.getLastUpdateTime() > 250) {
            RFToolsBaseMessages.sendToServer(PacketGetMonitorLog.create(te.getBlockPos()));
            te.setLastUpdateTime(time);
        }
        state.info = te.getInfo();
        state.data = te.getClientData();
        state.orientation = te.getBlockOrientation();
        state.mode = te.getMode();
    }

    @Override
    public void submit(State state, PoseStack pose, SubmitNodeCollector collector,
            net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        if (state.info != null && state.orientation != null) {
            state.info.render(state.mode, pose, collector, state.data, state.orientation, 0.3f);
        }
    }

    public static void register() {
        BlockEntityRenderers.register(InformationScreenModule.TYPE_INFORMATION_SCREEN.get(), InformationScreenRenderer::new);
    }
}
