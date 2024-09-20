package mcjty.rftoolsbase.modules.various.items;

import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.compat.patchouli.PatchouliCompatibility;
import mcjty.lib.tooltips.ITooltipSettings;
import mcjty.lib.varia.Tools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.key;

public class ManualItem extends Item implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = Lazy.of(() -> new TooltipBuilder()
            .info(key("message.rftoolsbase.shiftmessage"))
            .infoShift(header()));

    public ManualItem() {
        super(Registration.createStandardProperties().stacksTo(1));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, @Nonnull Player playerIn, @Nonnull InteractionHand handIn) {
        if (!worldIn.isClientSide) {
            PatchouliCompatibility.openBookGUI((ServerPlayer) playerIn, ResourceLocation.fromNamespaceAndPath(RFToolsBase.MODID, "manual"));
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, TooltipContext context, @Nonnull List<Component> list, @Nonnull TooltipFlag flags) {
        super.appendHoverText(itemStack, context, list, flags);
        tooltipBuilder.get().makeTooltip(Tools.getId(this), itemStack, list, flags);
    }
}
