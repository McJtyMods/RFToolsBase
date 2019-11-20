package mcjty.rftoolsbase.modules.informationscreen.client;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.lib.client.HudRenderHelper;
import mcjty.lib.client.RenderHelper;
import mcjty.lib.typed.TypedMap;
import mcjty.rftoolsbase.modules.informationscreen.blocks.DefaultPowerInformationScreenInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DefaultPowerInformationRenderer {

    public static void renderGraphical(TypedMap data, Direction orientation, double x, double y, double z, double scale) {
        if (data == null || data.size() == 0) {
            return;
        }

        long energy = data.get(DefaultPowerInformationScreenInfo.ENERGY);
        long maxEnergy = data.get(DefaultPowerInformationScreenInfo.MAXENERGY);

        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
        GlStateManager.rotatef(-getHudAngle(orientation), 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(0.0F, -0.2500F, -0.4375F + 0.9f);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getInstance().gameRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        if (maxEnergy > 0) {
//            int mode = infoscreen.getMode();
            GlStateManager.translatef(-0.5F, 0.5F, 0.07F);
            float f3 = 0.0075F;
            GlStateManager.scaled(f3 * scale, -f3 * scale, f3);
            GlStateManager.normal3f(0.0F, 0.0F, 1.0F);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            long pct = energy * 100 / maxEnergy;
            for (int i = 0 ; i < 100 ; i += 5) {
                int col = i < pct ? getPercentageColor(i) : 0xff111111;
                RenderHelper.drawFlatBox(16, (int) (100-i*.8-13), 88 , (int) (100-i*.8+3-13), col, col);
            }
        }
        Minecraft.getInstance().gameRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
    }

    public static void renderDefault(TypedMap data, Direction orientation, double x, double y, double z, double scale) {
        List<String> log = getLog(data);
        HudRenderHelper.HudPlacement hudPlacement = HudRenderHelper.HudPlacement.HUD_FRONT;
        HudRenderHelper.HudOrientation hudOrientation = HudRenderHelper.HudOrientation.HUD_SOUTH;
        HudRenderHelper.renderHud(log, hudPlacement, hudOrientation, orientation, x - orientation.getXOffset() * .95, y, z - orientation.getZOffset() * .95, (float) (1.0f + scale));
    }

    private static List<String> getLog(TypedMap data) {
        List<String> list = new ArrayList<>();
        list.add("");

        if (data != null && data.size() > 0) {
            long energy = data.get(DefaultPowerInformationScreenInfo.ENERGY);
            long maxEnergy = data.get(DefaultPowerInformationScreenInfo.MAXENERGY);
            list.add(TextFormatting.BLUE + " RF: " + TextFormatting.WHITE + formatPower(energy));
            list.add(TextFormatting.BLUE + " Max: " + TextFormatting.WHITE + formatPower(maxEnergy));
//            if (infoscreen.getRfExtractPerTick() != -1) {
//                list.add(TextFormatting.BLUE + " Ext/t: " + TextFormatting.WHITE + formatPower(infoscreen.getRfExtractPerTick()));
//                list.add(TextFormatting.BLUE + " Ins/t: " + TextFormatting.WHITE + formatPower(infoscreen.getRfInsertedPerTick()));
//            }
        } else {
            list.add(TextFormatting.RED + " Not a powercell");
            list.add(TextFormatting.RED + " or anything that");
            list.add(TextFormatting.RED + " supports power");
        }
        return list;
    }

    private static DecimalFormat format = new DecimalFormat("#.###");

    private static String formatPower(long l) {
        if (l < 100000) {
            return Long.toString(l);
        } else if (l < 10000000) {
            Double d = l / 1000.0;
            return format.format(d)+"K";
        } else if (l < 10000000000L) {
            Double d = l / 1000000.0;
            return format.format(d)+"M";
        } else {
            Double d = l / 1000000000.0;
            return format.format(d)+"G";
        }
    }

    private static float getHudAngle(Direction orientation) {
        float f3 = 0.0f;

        if (orientation != null) {
            switch (orientation) {
                case NORTH:
                    f3 = 180.0F;
                    break;
                case WEST:
                    f3 = 90.0F;
                    break;
                case EAST:
                    f3 = -90.0F;
                    break;
                default:
                    f3 = 0.0f;
            }
        }
        return f3;
    }

    private static int getPercentageColor(int i) {
        int col;
        if (i < 30) {
            col = 0xff00ff00;
        } else if (i < 40) {
            col = 0xff22dd00;
        } else if (i < 50) {
            col = 0xff44bb00;
        } else if (i < 60) {
            col = 0xff669900;
        } else if (i < 70) {
            col = 0xff887700;
        } else if (i < 80) {
            col = 0xffaa5500;
        } else if (i < 90) {
            col = 0xffcc3300;
        } else {
            col = 0xffee1100;
        }
        return col;
    }

}
