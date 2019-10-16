package mcjty.rftoolsbase.api.screens;


import mcjty.rftoolsbase.api.screens.data.IModuleDataBoolean;
import mcjty.rftoolsbase.api.screens.data.IModuleDataContents;
import mcjty.rftoolsbase.api.screens.data.IModuleDataInteger;
import mcjty.rftoolsbase.api.screens.data.IModuleDataString;

/**
 * Helper to create IScreenData instances for simple and common objects
 */
public interface IScreenDataHelper {

    IModuleDataInteger createInteger(int i);

    IModuleDataBoolean createBoolean(boolean b);

    IModuleDataString createString(String b);

    IModuleDataContents createContents(long contents, long maxContents, long lastPerTick);
}
