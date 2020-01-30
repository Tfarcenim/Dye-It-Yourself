package xyz.poketech.diy.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import xyz.poketech.diy.util.color.ColorUtil;
import xyz.poketech.diy.util.color.NBTColorUtil;

import java.awt.*;

/**
 * Layer to override the sheep wool
 * Based on {@link net.minecraft.client.renderer.entity.layers.SheepWoolLayer}
 */
public class LayerSheepWoolOverride extends LayerRenderer<SheepEntity, SheepModel<SheepEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final SheepWoolModel<SheepEntity> sheepModel = new SheepWoolModel<>();

    public LayerSheepWoolOverride(SheepRenderer sheepRendererIn) {
        super(sheepRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, SheepEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible()) {
            //Only render if a color is set
            if (entitylivingbaseIn.getPersistentData().contains(NBTColorUtil.COLOR_KEY)) {
                if(entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomName().getUnformattedComponentText())) {
                    return; //Don't render on top of jeb_ sheep
                }
            }
            Vector3f color = ColorUtil.toFloat(entitylivingbaseIn.getPersistentData().getInt(NBTColorUtil.COLOR_KEY));
            renderCopyCutoutModel(this.getEntityModel(), this.sheepModel, TEXTURE, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, color.getX(), color.getY(), color.getZ());
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
