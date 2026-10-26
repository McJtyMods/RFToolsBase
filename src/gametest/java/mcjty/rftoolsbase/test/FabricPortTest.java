package mcjty.rftoolsbase.test;

import java.util.ArrayList;
import mcjty.lib.api.container.CapabilityContainerProvider;
import mcjty.lib.api.container.DefaultContainerProvider;
import mcjty.lib.client.AbstractDynamicBakedModel;
import mcjty.lib.container.MenuTools;
import mcjty.rftoolsbase.modules.infuser.MachineInfuserModule;
import mcjty.rftoolsbase.modules.infuser.client.GuiMachineInfuser;
import mcjty.rftoolsbase.modules.various.VariousModule;
import mcjty.rftoolspower.modules.generator.CoalGeneratorModule;
import mcjty.rftoolspower.modules.generator.client.GuiCoalGenerator;
import mcjty.rftoolspower.modules.powercell.PowerCellModule;
import mcjty.rftoolspower.modules.powercell.blocks.PowerCellTileEntity;
import mcjty.xnet.apiimpl.items.NativeItems;
import mcjty.xnet.modules.cables.CableColor;
import mcjty.xnet.modules.cables.CableModule;
import mcjty.xnet.modules.cables.blocks.ConnectorTileEntity;
import mcjty.xnet.modules.cables.blocks.GenericCableBlock;
import mcjty.xnet.modules.controller.ControllerModule;
import mcjty.xnet.modules.controller.client.GuiController;
import mcjty.xnet.modules.facade.FacadeModule;
import mcjty.xnet.modules.facade.blocks.FacadeTileEntity;
import mcjty.xnet.modules.wireless.WirelessRouterModule;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import team.reborn.energy.api.EnergyStorage;

/** Runs in Minecraft with native mixins, registries, menus and rendered models. */
public class FabricPortTest implements FabricClientGameTest {
    private static final BlockPos INFUSER = new BlockPos(0, -59, 0);
    private static final BlockPos GENERATOR = new BlockPos(2, -59, 0);
    private static final BlockPos CELL = new BlockPos(4, -59, 0);
    private static final BlockPos CONNECTOR = new BlockPos(7, -59, 0);
    private static final BlockPos CONTROLLER = new BlockPos(8, -59, 0);
    private static final BlockPos FACADE = new BlockPos(10, -59, 0);

    @Override public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            context.waitFor(client -> client.player != null && client.level != null);
            world.getServer().runOnServer(server -> {
                var level = server.overworld();
                var player = server.getPlayerList().getPlayers().getFirst();
                player.setGameMode(GameType.SURVIVAL);
                place(level, INFUSER, MachineInfuserModule.MACHINE_INFUSER.block().get(), player);
                place(level, GENERATOR, CoalGeneratorModule.COALGENERATOR.block().get(), player);
                place(level, CELL, PowerCellModule.CELL1.block().get(), player);
                place(level, CONNECTOR, CableModule.CONNECTOR.get(), player);
                place(level, CONTROLLER, ControllerModule.CONTROLLER.block().get(), player);
                place(level, FACADE, FacadeModule.FACADE.get(), player);
                var facade = (FacadeTileEntity)level.getBlockEntity(FACADE);
                facade.setMimicBlock(Blocks.GOLD_BLOCK.defaultBlockState());
                place(level, new BlockPos(12, -59, 0), WirelessRouterModule.ANTENNA.get(), player);
                place(level, new BlockPos(14, -59, 0), WirelessRouterModule.ANTENNA_BASE.get(), player);
                place(level, new BlockPos(16, -59, 0), WirelessRouterModule.ANTENNA_DISH.get(), player);
                testEnergy(level);
                testItems(level, player);
                testFluidRounding();
                testFacadePersistence(level);
                var fuel = ItemStorage.SIDED.find(level, GENERATOR, Direction.UP);
                check(fuel != null, "Generator exposes native item storage");
                try (var transaction = Transaction.openOuter()) {
                    check(fuel.insert(ItemVariant.of(Items.COAL), 1, transaction) == 1, "Generator accepts coal");
                    transaction.commit();
                }
            });
            context.waitTicks(30);
            world.getServer().runOnServer(server -> {
                var energy = EnergyStorage.SIDED.find(server.overworld(), GENERATOR, Direction.UP);
                check(energy != null && energy.getAmount() > 0, "Coal generator produces native energy");
            });
            context.runOnClient(client -> {
                // Holding a connector renders the facade outlines through OVERLAY_LINES.
                client.player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(CableModule.CONNECTOR.get()));
                for (var pos : java.util.List.of(CELL, CONNECTOR, FACADE, new BlockPos(12, -59, 0),
                        new BlockPos(14, -59, 0), new BlockPos(16, -59, 0))) {
                    var state = client.level.getBlockState(pos);
                    var model = client.getModelManager().getBlockStateModelSet().get(state);
                    var parts = new ArrayList<BlockStateModelPart>();
                    if (model instanceof AbstractDynamicBakedModel dynamic) {
                        dynamic.collectParts(client.level, pos, state, RandomSource.create(1), parts);
                    } else model.collectParts(RandomSource.create(1), parts);
                    check(!parts.isEmpty(), "Block model is populated at " + pos);
                    int quads = 0;
                    for (var part : parts) {
                        for (var quad : part.getQuads(null)) {
                            quads++;
                            check(!quad.materialInfo().sprite().contents().name().getPath().equals("missingno"), "Model texture exists at " + pos);
                        }
                    }
                    check(quads > 0, "Block model has geometry at " + pos);
                }
            });
            context.waitTicks(5);
            // Verify common packet codecs, synchronized attachments and each mod's menu registration.
            open(context, world.getServer(), INFUSER, GuiMachineInfuser.class);
            open(context, world.getServer(), GENERATOR, GuiCoalGenerator.class);
            open(context, world.getServer(), CONTROLLER, GuiController.class);
            world.getServer().runOnServer(server -> {
                var level = server.overworld();
                var player = server.getPlayerList().getPlayers().getFirst();
                player.gameMode.destroyBlock(FACADE);
                check(level.getBlockState(FACADE).is(CableModule.NETCABLE.get()), "Breaking a facade preserves its cable");
                var connector = (ConnectorTileEntity)level.getBlockEntity(CONNECTOR);
                connector.setMimicBlock(Blocks.GOLD_BLOCK.defaultBlockState());
                player.gameMode.destroyBlock(CONNECTOR);
                check(level.getBlockEntity(CONNECTOR) == connector, "Breaking a connector facade preserves its block entity");
                check(connector.getMimicBlock() == null, "Breaking a connector facade removes only the mimic");
            });
        }
    }

    private static void place(ServerLevel level, BlockPos pos, Block block, ServerPlayer player) {
        level.setBlockAndUpdate(pos, block.defaultBlockState());
        block.setPlacedBy(level, pos, level.getBlockState(pos), player, new ItemStack(block));
    }

    private static void testEnergy(ServerLevel level) {
        var input = EnergyStorage.SIDED.find(level, INFUSER, Direction.UP);
        check(input != null, "Infuser exposes native energy input");
        try (var transaction = Transaction.openOuter()) { check(input.insert(1000, transaction) > 0, "Infuser accepts energy"); }
        check(input.getAmount() == 0, "Aborted infuser energy rolls back");
        try (var transaction = Transaction.openOuter()) { input.insert(1000, transaction); transaction.commit(); }
        check(input.getAmount() > 0, "Committed infuser energy persists");
        var cell = (PowerCellTileEntity)level.getBlockEntity(CELL);
        cell.toggleMode(Direction.UP);
        var storage = EnergyStorage.SIDED.find(level, CELL, Direction.UP);
        check(storage != null, "Power cell exposes native sided energy");
        long before = storage.getAmount();
        try (var transaction = Transaction.openOuter()) { check(storage.insert(1000, transaction) > 0, "Power cell accepts energy"); }
        check(storage.getAmount() == before, "Power cell rollback restores network totals");
        var connector = (ConnectorTileEntity)level.getBlockEntity(CONNECTOR);
        connector.setEnergyInputFrom(Direction.UP, 1000);
        var connectorInput = EnergyStorage.SIDED.find(level, CONNECTOR, Direction.UP);
        try (var transaction = Transaction.openOuter()) { check(connectorInput.insert(1000, transaction) > 0, "Connector accepts native energy"); }
        check(connector.getEnergy() == 0, "Connector rollback restores energy and side budget");
    }

    private static void testFluidRounding() {
        var fluid = net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER);
        var target = new net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage<net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant>() {
            @Override protected net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant getBlankVariant() {
                return net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.blank();
            }
            @Override protected long getCapacity(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant variant) { return 100; }
        };
        try {
            var method = mcjty.xnet.apiimpl.fluids.FluidChannelSettings.class.getDeclaredMethod("insertWholeMillibuckets",
                    net.fabricmc.fabric.api.transfer.v1.storage.Storage.class, mcjty.lib.varia.FluidStack.class,
                    net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext.class);
            method.setAccessible(true);
            try (var transaction = Transaction.openOuter()) {
                int moved = (int)method.invoke(null, target, new mcjty.lib.varia.FluidStack(fluid, 1000), transaction);
                check(moved == 1 && target.amount == 81, "Fluid transfer moves whole millibuckets without losing droplets");
            }
            check(target.amount == 0, "Aborted fluid transfer restores the destination");
        } catch (ReflectiveOperationException exception) { throw new AssertionError(exception); }
    }

    private static void testFacadePersistence(ServerLevel level) {
        var legacy = new net.minecraft.nbt.CompoundTag();
        legacy.putString("Name", "minecraft:oak_log");
        var properties = new net.minecraft.nbt.CompoundTag();
        properties.putString("axis", "x");
        legacy.put("Properties", properties);
        var root = new net.minecraft.nbt.CompoundTag();
        root.put("mimic", legacy);
        var expected = Blocks.OAK_LOG.defaultBlockState()
                .setValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS, Direction.Axis.X);
        check(mcjty.xnet.modules.facade.MimicBlockSupport.readFromNBT(level.registryAccess(), root).equals(expected),
                "Legacy facade NBT preserves the block and its properties");
        var ops = net.minecraft.resources.RegistryOps.create(net.minecraft.nbt.NbtOps.INSTANCE, level.registryAccess());
        var attachment = new net.minecraft.nbt.CompoundTag();
        attachment.put("state", legacy);
        var codec = mcjty.xnet.modules.facade.data.MimicData.CODEC;
        var decoded = codec.parse(ops, attachment).getOrThrow();
        check(decoded.state().equals(expected), "Legacy facade attachment preserves block properties");
        check(codec.parse(ops, codec.encodeStart(ops, decoded).getOrThrow()).getOrThrow().equals(decoded),
                "Facade attachment round trip uses the 26.3 block-state format");
    }

    private static void testItems(ServerLevel level, ServerPlayer player) {
        var sourcePos = new BlockPos(0, -59, 4);
        var targetPos = new BlockPos(2, -59, 4);
        place(level, sourcePos, Blocks.CHEST, player);
        place(level, targetPos, Blocks.CHEST, player);
        var source = (ChestBlockEntity)level.getBlockEntity(sourcePos);
        var target = (ChestBlockEntity)level.getBlockEntity(targetPos);
        source.setItem(0, new ItemStack(Items.DIAMOND, 12));
        var from = new NativeItems(ItemStorage.SIDED.find(level, sourcePos, Direction.UP));
        var to = new NativeItems(ItemStorage.SIDED.find(level, targetPos, Direction.UP));
        try {
            NativeItems.transfer(() -> {
                var stack = from.extractItem(0, 5, false);
                check(to.insertItem(0, stack, false).isEmpty(), "Native item insertion succeeds");
                throw new IllegalStateException("test rollback");
            });
            throw new AssertionError("Rollback test must throw");
        } catch (IllegalStateException expected) { check(expected.getMessage().equals("test rollback"), "Expected test abort"); }
        check(source.getItem(0).getCount() == 12 && target.isEmpty(), "Aborted native transfer restores both inventories");
        NativeItems.transfer(() -> {
            var stack = from.extractItem(0, 5, false);
            check(to.insertItem(0, stack, false).isEmpty(), "Native transfer commits");
            return 0;
        });
        check(source.getItem(0).getCount() == 7 && target.getItem(0).getCount() == 5, "Native item transfer conserves items");
    }

    private static void open(ClientGameTestContext context,
            net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext server,
            BlockPos pos, Class<? extends net.minecraft.client.gui.screens.Screen> screen) {
        server.runOnServer(instance -> {
            var level = instance.overworld();
            var player = instance.getPlayerList().getPlayers().getFirst();
            player.teleportTo(pos.getX() + .5, pos.getY() + 1, pos.getZ() + 2);
            var provider = CapabilityContainerProvider.CONTAINER_PROVIDER_CAPABILITY.find(level, pos, null);
            check(provider != null, "Machine exposes an extended menu at " + pos);
            MenuTools.open(player, provider, data -> {
                data.writeBlockPos(pos);
                if (provider instanceof DefaultContainerProvider<?> defaults) defaults.writeExtraData(data, level.getBlockEntity(pos));
            });
        });
        context.waitForScreen(screen);
        context.waitTicks(2);
        context.setScreen(() -> null);
        context.waitTicks(2);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
