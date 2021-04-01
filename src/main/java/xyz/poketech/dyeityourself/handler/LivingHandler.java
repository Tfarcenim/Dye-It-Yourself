package xyz.poketech.dyeityourself.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.poketech.dyeityourself.DyeItYourself;
import xyz.poketech.dyeityourself.ai.EatFlowerGoal;
import xyz.poketech.dyeityourself.network.RequestColorPacket;
import xyz.poketech.dyeityourself.util.RandomUtil;

@Mod.EventBusSubscriber(modid = DyeItYourself.MODID)
public class LivingHandler {

    public static final String NEXT_DYE_KEY = "nextDye";

    @SubscribeEvent
    public static void onEntityEnterWorld(EntityJoinWorldEvent event) {
        //Sync the sheep color on the client
        if (event.getEntity() instanceof SheepEntity) {
            SheepEntity sheep = ((SheepEntity) event.getEntity());

            if (event.getWorld().isRemote) {
                DyeItYourself.NETWORK.sendToServer(new RequestColorPacket(event.getEntity().getEntityId()));
            }

            else if (DyeItYourself.CONFIG.sheepEatFlowers.get()) {
                sheep.goalSelector.addGoal(5, new EatFlowerGoal(sheep));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!entity.world.isRemote && DyeItYourself.CONFIG.doDropDye.get() && entity instanceof SheepEntity) {

            SheepEntity sheep = (SheepEntity) event.getEntityLiving();
            CompoundNBT data = sheep.getPersistentData();

            if (data.contains(NEXT_DYE_KEY)) {
                int nextDye = data.getInt(NEXT_DYE_KEY);
                if (nextDye == 1) {
                    //Spawn a random amount of dye
                    int count = RandomUtil.getDyeDropAmountSafe();
                    if (count != 0) {
                        InventoryHelper.spawnItemStack(sheep.world, sheep.getPosX(), sheep.getPosY(), sheep.getPosZ(), new ItemStack(DyeItem.getItem(sheep.getFleeceColor()), count));

                        //Play the chicken egg sound
                        float pitch = (sheep.getRNG().nextFloat() - sheep.getRNG().nextFloat()) * 0.2F + 1.0F;
                        sheep.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, pitch);
                    }

                    //Set new dye
                    data.putInt(NEXT_DYE_KEY, RandomUtil.getNextDye());
                } else {
                    data.putInt(NEXT_DYE_KEY, --nextDye);
                }
            } else {
                data.putInt(NEXT_DYE_KEY, RandomUtil.getNextDye());
            }
        }
    }
}
