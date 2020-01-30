package xyz.poketech.diy.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartCommandBlockEntity;
import xyz.poketech.diy.DyeItYourself;
import xyz.poketech.diy.util.color.NBTColorUtil;

public class DistHelper {
    public static void updateSheepColor(int entityId, int color) {
        if (Minecraft.getInstance().world == null) {
            DyeItYourself.LOGGER.error("Minecraft.getInstance().world was null! That shouldn't happen.");
            return;
        }
        Entity entity = Minecraft.getInstance().world.getEntityByID(entityId);

        if (entity != null) {
            entity.getPersistentData().putInt(NBTColorUtil.COLOR_KEY, color);
        }
    }
}
