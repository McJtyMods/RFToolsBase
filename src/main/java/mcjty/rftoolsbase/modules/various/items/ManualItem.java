package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.compat.patchouli.PatchouliCompatibility;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import mcjty.lib.varia.Lazy;

import javax.annotation.Nonnull;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.key;

public class ManualItem extends Item implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(() -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header()));

    public ManualItem(net.minecraft.world.item.Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Nonnull
    @Override
    public InteractionResult use(Level worldIn, @Nonnull Player playerIn, @Nonnull InteractionHand handIn) {
        if (!worldIn.isClientSide()) {
            PatchouliCompatibility.openBookGUI((ServerPlayer) playerIn, Identifier.fromNamespaceAndPath(RFToolsBase.MODID, "manual"));
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, TooltipContext context, @Nonnull net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> consumer, @Nonnull TooltipFlag flags) {
        List<Component> list = new java.util.ArrayList<>();
        super.appendHoverText(itemStack, context, display, consumer, flags);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flags);

        list.forEach(consumer);
    }
}
