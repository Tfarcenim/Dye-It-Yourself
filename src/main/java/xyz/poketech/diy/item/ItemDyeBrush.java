package xyz.poketech.diy.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.RandomUtils;
import xyz.poketech.diy.network.PacketHandler;
import xyz.poketech.diy.util.color.ColorUtil;
import xyz.poketech.diy.util.color.NBTColorUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemDyeBrush extends Item {

    public ItemDyeBrush(Properties properties) {
        super(properties);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if(!playerIn.world.isRemote) {
            if(target instanceof SheepEntity) {
                int color = NBTColorUtil.getColor(stack);
                target.getPersistentData().putInt(NBTColorUtil.COLOR_KEY, color);
                PacketHandler.sendColorUpdate(target.getEntityId(), color, target.getPosition(), playerIn.dimension, 25);
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!playerIn.world.isRemote && playerIn.getPose() == Pose.CROUCHING) {
            ItemStack itemStack = playerIn.getHeldItem(handIn);

            if(itemStack.getTag() == null) {
                itemStack.setTag(new CompoundNBT());
            }

            int r = RandomUtils.nextInt(0, 256);
            int g = RandomUtils.nextInt(0, 256);
            int b = RandomUtils.nextInt(0, 256);

            int color = ColorUtil.getRGB(r,g,b);

            itemStack.getTag().putInt(NBTColorUtil.COLOR_KEY, color);

            playerIn.sendStatusMessage(new TranslationTextComponent("tooltip.diy.current_color", r, g,b), true);
        }
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        int[] rgb = ColorUtil.toRGB(NBTColorUtil.getColor(stack));
        tooltip.add(new StringTextComponent("WIP"));
        tooltip.add(new TranslationTextComponent("tooltip.diy.current_color", rgb[0], rgb[1],rgb[2]));
        tooltip.add(new TranslationTextComponent("item.diy.dye_brush.tooltip"));
    }
}
