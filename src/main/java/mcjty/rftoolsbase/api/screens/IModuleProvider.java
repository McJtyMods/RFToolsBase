package mcjty.rftoolsbase.api.screens;

/**
 * Implement this interface on your module item.
 */
public interface IModuleProvider {

    Class<? extends IScreenModule<?>> getServerScreenModule();

    Class<? extends IClientScreenModule<?>> getClientScreenModule();

    String getModuleName();

    void createGui(IModuleGuiBuilder guiBuilder);
}
