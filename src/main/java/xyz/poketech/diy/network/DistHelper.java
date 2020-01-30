package xyz.poketech.diy.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.MinecartCommandBlockEntity;
import net.minecraft.entity.passive.SheepEntity;
import xyz.poketech.diy.DyeItYourself;
import xyz.poketech.diy.client.render.layer.LayerSheepWoolOverride;
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

    public static void addRenderLayers() {
        EntityRenderer renderer = Minecraft.getInstance().getRenderManager().renderers.get(EntityType.SHEEP);
        if (renderer instanceof SheepRenderer) {
            SheepRenderer sheep = (SheepRenderer) renderer;
            sheep.addLayer(new LayerSheepWoolOverride(sheep));
        }
    }
}
