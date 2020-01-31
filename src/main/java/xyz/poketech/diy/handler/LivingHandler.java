package xyz.poketech.diy.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.poketech.diy.DyeItYourself;
import xyz.poketech.diy.ai.EatFlowerGoal;
import xyz.poketech.diy.network.RequestColorPacket;
import xyz.poketech.diy.util.RandomUtil;
import xyz.poketech.diy.util.WorldUtil;

@Mod.EventBusSubscriber(modid = DyeItYourself.MODID)
public class LivingHandler {

    public static final String NEXT_DYE_KEY = "nextDye";

    @SubscribeEvent
    public static void onEntitySpawn(LivingSpawnEvent.EnteringChunk event) {
        if (event.getEntity() instanceof SheepEntity) {
            SheepEntity sheep = ((SheepEntity) event.getEntity());

            if (DyeItYourself.CONFIG.sheepEatFlowers.get()) {
                sheep.goalSelector.addGoal(5, new EatFlowerGoal(sheep));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityEnterWorld(EntityJoinWorldEvent event) {
        //Sync the sheep color on the client
        if(event.getWorld().isRemote) {
            if(event.getEntity() instanceof SheepEntity) {
                DyeItYourself.NETWORK.sendToServer(new RequestColorPacket(event.getEntity().getEntityId()));
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
                    if(count != 0) {
                        WorldUtil.spawnStack(sheep.world, sheep.getPosition(), new ItemStack(DyeItem.getItem(sheep.getFleeceColor()), count));

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
