package xyz.poketech.dyeityourself.util.color;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTColorUtil {

    public static final String COLOR_KEY = "diy_color";
    private static final int WHITE = ColorUtil.getRGB(255, 255, 255);

    public static void setEntityColor(Entity entity, int color) {
        entity.getPersistentData().putInt(COLOR_KEY, color);
    }

    public static void setEntityColor(Entity entity, int r, int g, int b) {
        setEntityColor(entity, ColorUtil.getRGB(r, g, b));
    }

    public static void removeEntityColor(Entity entity) {
        entity.getPersistentData().remove(COLOR_KEY);
    }

    public static int getColor(ItemStack stack) {
        if(stack.getTag() != null) {
            if(stack.getTag().contains(COLOR_KEY)) {
                return stack.getTag().getInt(COLOR_KEY);
            } else {
                stack.getTag().putInt(COLOR_KEY, WHITE);
            }
        } else {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt(COLOR_KEY, WHITE);
            stack.setTag(tag);
        }
        return WHITE;
    }
}
