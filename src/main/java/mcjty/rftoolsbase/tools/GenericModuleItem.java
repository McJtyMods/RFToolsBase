package mcjty.rftoolsbase.tools;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.rftoolsbase.api.screens.IModuleProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;

public abstract class GenericModuleItem extends Item implements IModuleProvider, ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
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

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        tooltipBuilder.get().makeTooltip(getRegistryName(), itemStack, list, flag);
    }
}
