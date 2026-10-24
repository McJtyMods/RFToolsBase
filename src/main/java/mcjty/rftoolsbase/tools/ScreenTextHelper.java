package mcjty.rftoolsbase.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.lib.client.RenderHelper;
import mcjty.rftoolsbase.api.screens.ITextRenderHelper;
import mcjty.rftoolsbase.api.screens.ModuleRenderInfo;
import mcjty.rftoolsbase.api.screens.TextAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ScreenTextHelper implements ITextRenderHelper {

    private boolean large = false;
    private TextAlign align = TextAlign.ALIGN_LEFT;

    private boolean dirty = true;
    private int textx;
    private String text;
    private boolean truetype = false;
    private Identifier fontId;

    public int getTextx() {
        return textx;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setDirty() {
        this.dirty = true;
    }

    @Override
    public boolean isLarge() {
        return large;
    }

    @Override
    public ITextRenderHelper large(boolean large) {
        dirty = true;
        this.large = large;
        return this;
    }

    @Override
    public TextAlign getAlign() {
        return align;
    }

    @Override
    public ITextRenderHelper align(TextAlign align) {
        dirty = true;
        this.align = align;
        return this;
    }

    @Override
    public void setup(String line, int width, ModuleRenderInfo renderInfo) {
        if ((!dirty) && truetype == renderInfo.truetype) {
            return;
        }
        dirty = false;
        truetype = renderInfo.truetype;
        fontId = renderInfo.fontId;
        Font renderer = getFontRenderer(truetype, fontId);

        textx = large ? 4 : 7;
        if (truetype) {
            width *= 2;
        }
        text = renderer.plainSubstrByWidth(line, (large ? (width / 8) : (width / 4)) - textx);
//            int w = large ? 58 : 115;
        int w = large ? (int) (width / 8.8f) : (int) (width / 4.45f);
        switch (align) {
            case ALIGN_LEFT:
                break;
            case ALIGN_CENTER:
                textx += (w - renderer.width(text)) / 2;
                break;
            case ALIGN_RIGHT:
                textx += w - renderer.width(text);
                break;
        }
    }

    @Override
    public void renderText(GuiGraphicsExtractor graphics, SubmitNodeCollector buffer, int x, int y, int color, ModuleRenderInfo renderInfo) {
        renderScaled(fontId, graphics, buffer, text, textx + x, y, color, truetype, renderInfo.getLightmapValue());
    }

    public static void renderScaled(Identifier fontId, GuiGraphicsExtractor graphics, SubmitNodeCollector buffer, String text, int x, int y, int color, boolean truetype, int lightmapValue) {
        var pose = graphics.pose();
        pose.pushMatrix();
        if (truetype) pose.scale(0.5f, 0.5f);
        var component = net.minecraft.network.chat.Component.literal(text);
        if (fontId != null) component.withStyle(style -> style.withFont(new net.minecraft.network.chat.FontDescription.Resource(fontId)));
        graphics.text(Minecraft.getInstance().font, component, truetype ? x * 2 : x, truetype ? y * 2 : y, 0xff000000 | color, false);
        pose.popMatrix();
    }

    public static void renderScaledTrimmed(Identifier fontId, PoseStack matrixStack, SubmitNodeCollector buffer, String text, int x, int y, int maxwidth, int color, boolean truetype, int lightmapValue) {
        Font renderer = getFontRenderer(truetype, fontId);
        if (truetype) {
            matrixStack.pushPose();
            matrixStack.scale(.5f, .5f, .5f);
            text = renderer.plainSubstrByWidth(text, maxwidth * 2);
            renderFontText(fontId, renderer, text, x * 2, y * 2, color, matrixStack, buffer, lightmapValue);
            matrixStack.popPose();
        } else {
            text = renderer.plainSubstrByWidth(text, maxwidth);
            renderFontText(fontId, renderer, text, x * 2, y * 2, color, matrixStack, buffer, lightmapValue);
        }
    }

    private static void renderFontText(Identifier fontId, Font font, String text, int x, int y, int color,
            PoseStack pose, SubmitNodeCollector collector, int light) {
        var component = net.minecraft.network.chat.Component.literal(text);
        if (fontId != null) component.withStyle(style -> style.withFont(new net.minecraft.network.chat.FontDescription.Resource(fontId)));
        collector.submitText(pose, x, y, component.getVisualOrderText(), false, Font.DisplayMode.NORMAL, light, color, 0, 0);
    }

    private static Font getFontRenderer(boolean truetype, Identifier fontId) {
        return Minecraft.getInstance().font;
    }
}
