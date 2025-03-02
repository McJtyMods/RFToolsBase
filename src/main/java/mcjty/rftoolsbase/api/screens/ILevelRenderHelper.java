package mcjty.rftoolsbase.api.screens;

import mcjty.rftoolsbase.api.screens.data.IModuleDataContents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This level render helper knows how to render progress/energy/level bars
 */
public interface ILevelRenderHelper {

    void render(GuiGraphics graphics, MultiBufferSource buffer, int x, int y, @Nullable IModuleDataContents data, @Nonnull ModuleRenderInfo renderInfo);

    ILevelRenderHelper label(String label);

    ILevelRenderHelper settings(boolean hidebar, BarMode barMode);

    ILevelRenderHelper color(int poscolor, int negcolor);

    ILevelRenderHelper gradient(int gradient1, int gradient2);

    ILevelRenderHelper format(FormatStyle formatStyle);

    // All getters
    int getPosColor();
    int getNegColor();
    int getGradient1();
    int getGradient2();
    FormatStyle getFormatStyle();
    BarMode getBarMode();
    boolean isHideBar();
    String getLabel();

    // All setters
    void setPosColor(int poscolor);
    void setNegColor(int negcolor);
    void setGradient1(int gradient1);
    void setGradient2(int gradient2);
    void setFormatStyle(FormatStyle formatStyle);
    void setBarMode(BarMode barMode);
    void setHideBar(boolean hidebar);
    void setLabel(String label);
}
