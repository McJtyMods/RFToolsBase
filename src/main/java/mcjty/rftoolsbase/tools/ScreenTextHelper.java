package mcjty.rftoolsbase.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.rftoolsbase.api.screens.ITextRenderHelper;
import mcjty.rftoolsbase.api.screens.ModuleRenderInfo;
import mcjty.rftoolsbase.api.screens.TextAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ScreenTextHelper implements ITextRenderHelper {

    private boolean large = false;
    private TextAlign align = TextAlign.ALIGN_LEFT;

    private boolean dirty = true;
    private int textx;
    private String text;
    private boolean truetype = false;
    private ResourceLocation fontId;

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
        FontRenderer renderer = getFontRenderer(truetype, fontId);

        textx = large ? 4 : 7;
        if (truetype) {
            width *= 2;
        }
        text = renderer.func_238412_a_(line, (large ? (width / 8) : (width / 4)) - textx);
//            int w = large ? 58 : 115;
        int w = large ? (int) (width / 8.8f) : (int) (width / 4.45f);
        switch (align) {
            case ALIGN_LEFT:
                break;
            case ALIGN_CENTER:
                textx += (w - renderer.getStringWidth(text)) / 2;
                break;
            case ALIGN_RIGHT:
                textx += w - renderer.getStringWidth(text);
                break;
        }
    }

    @Override
    public void renderText(MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y, int color, ModuleRenderInfo renderInfo) {
        renderScaled(fontId, matrixStack, buffer, text, textx + x, y, color, truetype, renderInfo.getLightmapValue());
    }

    public static void renderScaled(ResourceLocation fontId, MatrixStack matrixStack, IRenderTypeBuffer buffer, String text, int x, int y, int color, boolean truetype, int lightmapValue) {
        FontRenderer renderer = getFontRenderer(truetype, fontId);
        if (truetype) {
            matrixStack.push();
            matrixStack.scale(.5f, .5f, .5f);
            // @todo 1.15 check if the 0xff000000 | is needed
            renderer.renderString(text, x * 2, y * 2, 0xff000000 | color, false, matrixStack.getLast().getMatrix(), buffer, false, 0, lightmapValue);
            matrixStack.pop();
        } else {
            renderer.renderString(text, x, y, 0xff000000 | color, false, matrixStack.getLast().getMatrix(), buffer, false, 0, lightmapValue);
        }
    }

    public static void renderScaledTrimmed(ResourceLocation fontId, MatrixStack matrixStack, IRenderTypeBuffer buffer, String text, int x, int y, int maxwidth, int color, boolean truetype, int lightmapValue) {
        FontRenderer renderer = getFontRenderer(truetype, fontId);
        if (truetype) {
            matrixStack.push();
            matrixStack.scale(.5f, .5f, .5f);
            text = renderer.func_238412_a_(text, maxwidth * 2);
            renderer.renderString(text, x * 2, y * 2, color, false, matrixStack.getLast().getMatrix(), buffer, false, 0, lightmapValue);
            matrixStack.pop();
        } else {
            text = renderer.func_238412_a_(text, maxwidth);
            renderer.renderString(text, x * 2, y * 2, color, false, matrixStack.getLast().getMatrix(), buffer, false, 0, lightmapValue);
        }
    }

    private static Map<ResourceLocation, FontRenderer> trueTypeRenderer = new HashMap<>();

    private static FontRenderer getFontRenderer(boolean truetype, ResourceLocation fontId) {
        if (truetype) {
            if (!trueTypeRenderer.containsKey(fontId)) {
                Font font = Minecraft.getInstance().fontResourceMananger.field_238546_d_.get(fontId);
                        //new ResourceLocation("rftoolsutility", "ubuntu"));
                trueTypeRenderer.put(fontId, new FontRenderer(resourceLocation -> font));
            }
            return trueTypeRenderer.get(fontId);
        } else {
            return Minecraft.getInstance().fontRenderer;
        }
    }

}