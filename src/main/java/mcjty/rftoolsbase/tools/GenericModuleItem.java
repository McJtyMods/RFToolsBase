package mcjty.rftoolsbase.tools;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.rftoolsbase.api.screens.IModuleProvider;
import mcjty.rftoolsbase.tools.ModuleTools;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;

public abstract class GenericModuleItem extends Item implements IModuleProvider, ITooltipSettings {

    private final TooltipBuilder tooltipBuilder = new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(),
                    gold(this::hasGoldMessage),
                    parameter("uses", this::getUsesString),
                    parameter("info", this::getInfoString));

    protected boolean hasGoldMessage(ItemStack stack) {
        return false;
    }

    protected abstract int getUses(ItemStack stack);

    protected String getInfoString(ItemStack stack) {
        return null;
    }

    private String getUsesString(ItemStack stack) {
        return getUses(stack) + " RF/tick";
    }

    public GenericModuleItem(Properties properties) {
        super(properties);
    }

    protected boolean hasTarget(ItemStack stack) {
        return ModuleTools.hasModuleTarget(stack);
    }

    protected String getTargetString(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            if (tag.contains("monitorx")) {
                int monitorx = tag.getInt("monitorx");
                int monitory = tag.getInt("monitory");
                int monitorz = tag.getInt("monitorz");
                String monitorname = tag.getString("monitorname");
                String monitordim = tag.getString("monitordim");
                if (!monitordim.isEmpty()) {
                    return monitorname + " (at " + monitorx + "," + monitory + "," + monitorz + ", " + monitordim + ")";
                } else {
                    return monitorname + " (at " + monitorx + "," + monitory + "," + monitorz + ")";
                }
            }
        }
        return "<unset>";
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        tooltipBuilder.makeTooltip(getRegistryName(), itemStack, list, flag);
    }
}
