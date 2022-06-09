package mcjty.rftoolsbase.tools;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.api.screens.IModuleProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
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
    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable Level world, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flag);
    }
}
