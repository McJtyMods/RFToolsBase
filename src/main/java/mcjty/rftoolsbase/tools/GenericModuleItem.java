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
import mcjty.lib.varia.Lazy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;

public abstract class GenericModuleItem extends Item implements IModuleProvider, ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(() -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header(),
                    gold(this::hasGoldMessage),
                    parameter("uses", this::getUsesString),
                    parameter("info", this::getInfoString)));

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

    public boolean isPlusModule() {
        return false;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, TooltipContext context, @Nonnull net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> consumer, @Nonnull TooltipFlag flag) {
        List<Component> list = new java.util.ArrayList<>();
        super.appendHoverText(itemStack, context, display, consumer, flag);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flag);

        list.forEach(consumer);
    }
}
