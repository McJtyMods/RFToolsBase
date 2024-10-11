package mcjty.rftoolsbase.api.xnet.channels;

import com.google.gson.JsonObject;
import mcjty.rftoolsbase.api.xnet.gui.IEditorGui;
import mcjty.rftoolsbase.api.xnet.gui.IndicatorIcon;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Channel type specific connector settings
 */
public interface IConnectorSettings {

    IChannelType getType();

    void readFromNBT(CompoundTag tag);

    void writeToNBT(CompoundTag tag);

    /**
     * Write the connector settings to json. The default implementation does nothing.
     * Return null if your connector does not support this. This feature is used for
     * copy/pasting channels.
     */
    default JsonObject writeToJson() { return null; }

    /**
     * Initialize this connector from the JSon data.
     */
    default void readFromJson(JsonObject data) {
    }

    /**
     * Return an optional indicator icon
     */
    @Nullable
    IndicatorIcon getIndicatorIcon();

    /**
     * Return a one-char indicator of the current status. If this is
     * present it is drawn on top of the existing icon.
     */
    @Nullable
    String getIndicator();

    /**
     * Return true if a tag is enabled given the current settings
     */
    boolean isEnabled(String tag);

    /**
     * Create the gui for this connector and fill with the current values or
     * defaults if it is not set yet. This is called client-side.
     */
    void createGui(IEditorGui gui);

    /**
     * If something changes on the gui then this will be called server
     * side with a map for all gui components
     */
    void update(Map<String, Object> data);
}
