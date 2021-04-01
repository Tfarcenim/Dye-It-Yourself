package xyz.poketech.dyeityourself.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import xyz.poketech.dyeityourself.network.PacketHandler;
import xyz.poketech.dyeityourself.util.color.ColorUtil;
import xyz.poketech.dyeityourself.util.color.NBTColorUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DyeBrushItem extends Item {

    public DyeBrushItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if(!playerIn.world.isRemote) {
            if(target instanceof SheepEntity) {
                int color = NBTColorUtil.getColor(stack);
                target.getPersistentData().putInt(NBTColorUtil.COLOR_KEY, color);
                PacketHandler.sendColorUpdate(target.getEntityId(), color, target.getPosition(), playerIn.world.getDimensionKey(), 25);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
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

            playerIn.sendStatusMessage(new TranslationTextComponent("tooltip.dyeityourself.current_color", r, g,b), true);
        }
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        int[] rgb = ColorUtil.toRGB(NBTColorUtil.getColor(stack));
        tooltip.add(new StringTextComponent("WIP"));
        tooltip.add(new TranslationTextComponent("tooltip.dyeityourself.current_color", rgb[0], rgb[1],rgb[2]));
        tooltip.add(new TranslationTextComponent("item.dyeityourself.dye_brush.tooltip"));
    }
}
