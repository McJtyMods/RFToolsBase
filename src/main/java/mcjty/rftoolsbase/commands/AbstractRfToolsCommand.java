package mcjty.rftoolsbase.commands;

import mcjty.lib.varia.ComponentFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractRfToolsCommand implements RfToolsCommand {

    protected String fetchString(Player sender, String[] args, int index, String defaultValue) {
        try {
            return args[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    protected boolean fetchBool(Player sender, String[] args, int index, boolean defaultValue) {
        boolean value;
        try {
            value = Boolean.parseBoolean(args[index]);
        } catch (NumberFormatException e) {
            value = false;
            Component component = ComponentFactory.literal(ChatFormatting.RED + "Parameter is not a valid boolean!");
            if (sender != null) {
                sender.displayClientMessage(component, false);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
        return value;
    }

    protected int fetchInt(Player sender, String[] args, int index, int defaultValue) {
        int value;
        try {
            value = Integer.parseInt(args[index]);
        } catch (NumberFormatException e) {
            value = 0;
            Component component = ComponentFactory.literal(ChatFormatting.RED + "Parameter is not a valid integer!");
            if (sender != null) {
                sender.displayClientMessage(component, false);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
        return value;
    }

    protected float fetchFloat(Player sender, String[] args, int index, float defaultValue) {
        float value;
        try {
            value = Float.parseFloat(args[index]);
        } catch (NumberFormatException e) {
            value = 0.0f;
            Component component = ComponentFactory.literal(ChatFormatting.RED + "Parameter is not a valid real number!");
            if (sender != null) {
                sender.displayClientMessage(component, false);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public boolean isClientSide() {
        return false;
    }
}
