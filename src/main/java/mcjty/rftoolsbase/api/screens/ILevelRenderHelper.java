package mcjty.rftoolsbase.api.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.rftoolsbase.api.screens.data.IModuleDataContents;
import net.minecraft.client.renderer.MultiBufferSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This level render helper knows how to render progress/energy/level bars
 */
public interface ILevelRenderHelper {

    void render(PoseStack matrixStack, MultiBufferSource buffer, int x, int y, @Nullable IModuleDataContents data, @Nonnull ModuleRenderInfo renderInfo);

    ILevelRenderHelper label(String label);

    ILevelRenderHelper settings(boolean hidebar, boolean hidetext, boolean showpct, boolean showdiff);

    ILevelRenderHelper color(int poscolor, int negcolor);

    ILevelRenderHelper gradient(int gradient1, int gradient2);

    ILevelRenderHelper format(FormatStyle formatStyle);
}
