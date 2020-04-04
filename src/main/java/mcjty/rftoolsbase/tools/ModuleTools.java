package mcjty.rftoolsbase.tools;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class ModuleTools {

    public static boolean hasModuleTarget(ItemStack stack) {
        if (!stack.hasTag()) {
            return false;
        }
        return stack.getTag().contains("monitorx");
    }

    //    public static int getDimensionFromModule(ItemStack stack) {
//        if (!stack.hasTag()) {
//            return 0;
//        }
//        return stack.getTag().getInt("monitordim");
//    }
//
    public static void setPositionInModule(ItemStack stack, DimensionType dimension, BlockPos pos, String name) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (dimension != null) {
            tag.putString("monitordim", dimension.getRegistryName().toString());
        }
        if (name != null) {
            tag.putString("monitorname", name);
        }
        tag.putInt("monitorx", pos.getX());
        tag.putInt("monitory", pos.getY());
        tag.putInt("monitorz", pos.getZ());
    }

    public static void clearPositionInModule(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.remove("monitordim");
        tag.remove("monitorx");
        tag.remove("monitory");
        tag.remove("monitorz");
        tag.remove("monitorname");
    }

    public static BlockPos getPositionFromModule(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        int monitorx = tag.getInt("monitorx");
        int monitory = tag.getInt("monitory");
        int monitorz = tag.getInt("monitorz");
        return new BlockPos(monitorx, monitory, monitorz);
    }

    public static String getTargetString(ItemStack stack) {
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
}
