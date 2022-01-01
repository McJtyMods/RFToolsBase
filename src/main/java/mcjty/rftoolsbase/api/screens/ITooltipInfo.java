package mcjty.rftoolsbase.api.screens;

import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Implement this interface on your server side screen module
 * (typically the class that also implements IScreenModule) to
 * have support for tooltips (for things like WAILA, TOP, ...)
 */
public interface ITooltipInfo {
    List<String> getInfo(Level world, int x, int y);
}
