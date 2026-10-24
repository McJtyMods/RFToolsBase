package mcjty.rftoolsbase.api.xnet.gui;

import net.minecraft.resources.Identifier;

public class IndicatorIcon {
    private final Identifier image;
    private final int u;
    private final int v;
    private final int iw;
    private final int ih;

    public IndicatorIcon(Identifier image, int u, int v, int iw, int ih) {
        this.image = image;
        this.u = u;
        this.v = v;
        this.iw = iw;
        this.ih = ih;
    }

    public Identifier getImage() {
        return image;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getIw() {
        return iw;
    }

    public int getIh() {
        return ih;
    }
}
