package mcjty.rftoolsbase.tools;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

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
        void tickOnServer();
        ResourceKey<Level> getDimension();
    }

    private static final Map<ResourceKey<Level>, List<IOrderTicker>[]> tiles = new HashMap<>();
    private static long ticker = 0;     // To keep track of when a tile was added

    public static void clean() {
        tiles.clear();
    }

    // Return a number that you can use to detect that you have already been added to the queue this tick
    public static long getTicker() {
        return ticker;
    }

    public static void queue(IOrderTicker tile) {
        if (!tiles.containsKey(tile.getDimension())) {
            List<IOrderTicker>[] list = new List[Rank.values().length];
            for (Rank rank : Rank.values()) {
                list[rank.ordinal()] = new ArrayList<>();
            }
            tiles.put(tile.getDimension(), list);
        }
        tiles.get(tile.getDimension())[tile.getRank().ordinal()].add(tile);
    }

    private static <T extends IOrderTicker> void tickServer(List<T> tileEntities) {
        for (T tileEntity : tileEntities) {
            tileEntity.tickOnServer();
        }
        tileEntities.clear();
    }

    public static void postWorldTick(ResourceKey<Level> dimension) {
        ticker++;
        List<IOrderTicker>[] lists = tiles.get(dimension);
        if (lists != null) {
            for (Rank rank : Rank.values()) {
                tickServer(lists[rank.ordinal()]);
            }
        }
    }
}
