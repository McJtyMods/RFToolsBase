package mcjty.rftoolsbase.modules.filter.network;

import mcjty.lib.network.CustomPacketPayload;
import mcjty.lib.network.PlayPayloadContext;
import mcjty.lib.network.TypedMapTools;
import mcjty.lib.typed.Key;
import mcjty.lib.typed.Type;
import mcjty.lib.typed.TypedMap;
import mcjty.lib.varia.TagTools;
import mcjty.rftoolsbase.RFToolsBase;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleContainer;
import mcjty.rftoolsbase.modules.filter.items.FilterModuleInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record PacketUpdateNBTItemFilter(TypedMap args) implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(RFToolsBase.MODID, "update_nbt_item_filter");

    private static final Key<Integer> CONTAINER_ID = new Key<>("containerId", Type.INTEGER);
    private static final Key<List<ItemStack>> ITEMS = new Key<>("Items", Type.ITEMSTACK_LIST);
    private static final Key<List<String>> TAGS = new Key<>("Tags", Type.STRING_LIST);
    private static final Key<Boolean> BLACKLIST = new Key<>("blacklistMode", Type.BOOLEAN);
    private static final Key<Boolean> DAMAGE = new Key<>("damageMode", Type.BOOLEAN);
    private static final Key<Boolean> NBT = new Key<>("nbtMode", Type.BOOLEAN);
    private static final Key<Boolean> MOD = new Key<>("modMode", Type.BOOLEAN);

    public static PacketUpdateNBTItemFilter create(FriendlyByteBuf buf) {
        return new PacketUpdateNBTItemFilter(TypedMapTools.readArguments(buf));
    }

    public static PacketUpdateNBTItemFilter updateEntries(int containerId, FilterModuleInventory inventory) {
        return new PacketUpdateNBTItemFilter(TypedMap.builder()
                .put(CONTAINER_ID, containerId)
                .put(ITEMS, inventory.getStacks())
                .put(TAGS, inventory.getTags().stream().map(TagKey::location).map(ResourceLocation::toString).toList())
                .build());
    }

    public static PacketUpdateNBTItemFilter updateModes(int containerId, boolean blacklist, boolean damage, boolean nbt, boolean mod) {
        return new PacketUpdateNBTItemFilter(TypedMap.builder()
                .put(CONTAINER_ID, containerId)
                .put(BLACKLIST, blacklist)
                .put(DAMAGE, damage)
                .put(NBT, nbt)
                .put(MOD, mod)
                .build());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        TypedMapTools.writeArguments(buf, args);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> ctx.player().ifPresent(this::updateFilter));
    }

    private void updateFilter(Player player) {
        Integer containerId = args.get(CONTAINER_ID);
        if (containerId == null || !(player.containerMenu instanceof FilterModuleContainer menu)
                || menu.containerId != containerId || !menu.stillValid(player)) {
            return;
        }

        ItemStack filter = player.getMainHandItem();
        List<ItemStack> items = args.get(ITEMS);
        List<String> tags = args.get(TAGS);
        if (items != null && tags != null) {
            FilterModuleInventory inventory = new FilterModuleInventory(filter);
            inventory.getStacks().clear();
            for (ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    inventory.getStacks().add(stack.copyWithCount(1));
                }
            }
            inventory.getTags().clear();
            for (String tag : tags) {
                ResourceLocation id = ResourceLocation.tryParse(tag);
                if (id != null) {
                    inventory.addTag(TagTools.createItemTagKey(id));
                }
            }
            inventory.markDirty();
        }

        CompoundTag tag = filter.getOrCreateTag();
        args.getOptional(BLACKLIST).ifPresent(value -> tag.putString(BLACKLIST.name(), value ? "Black" : "White"));
        args.getOptional(DAMAGE).ifPresent(value -> tag.putBoolean(DAMAGE.name(), value));
        args.getOptional(NBT).ifPresent(value -> tag.putBoolean(NBT.name(), value));
        args.getOptional(MOD).ifPresent(value -> tag.putBoolean(MOD.name(), value));
    }
}
