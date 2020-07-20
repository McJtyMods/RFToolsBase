package mcjty.rftoolsbase.tools;

import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use this class to ensure tile entities that are supposed to work together do this in a consistent way
 */
public class TickOrderHandler {

    /*
     * 0. Pearl Injector
     * 1. Endergenics
     * 2. Ender monitors
     * 3. Timers
     * 4. Sequencers
     */
    public enum Rank {
        RANK_0,
        RANK_1,
        RANK_2,
        RANK_3,
        RANK_4
    }

    public interface IOrderTicker {
        Rank getRank();
        void tickServer();
        DimensionType getDimension();
    }

    private static Map<DimensionType, List<IOrderTicker>[]> tiles = new HashMap<>();

    private TickOrderHandler() {
    }

    public static void clean() {
        tiles.clear();
    }

    public static void queue(IOrderTicker tile) {
        if (!tiles.containsKey(tile.getDimension())) {
            List<IOrderTicker>[] list = new List[Rank.values().length];
            for (Rank rank : Rank.values()) {
                list[rank.ordinal()] = new ArrayList<>();
            }
        }
        tiles.get(tile.getDimension())[tile.getRank().ordinal()].add(tile);
    }

    private static <T extends IOrderTicker> void tickServer(DimensionType dimension, List<T> tileEntities) {
        for (T tileEntity : tileEntities) {
            tileEntity.tickServer();
        }
        tileEntities.clear();
    }

    public static void postWorldTick(DimensionType dimension) {
        List<IOrderTicker>[] lists = tiles.get(dimension);
        if (lists != null) {
            for (Rank rank : Rank.values()) {
                tickServer(dimension, lists[rank.ordinal()]);
            }
        }
    }
}
