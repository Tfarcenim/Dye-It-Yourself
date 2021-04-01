package xyz.poketech.dyeityourself.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import xyz.poketech.dyeityourself.DyeItYourself;
import xyz.poketech.dyeityourself.util.DyeUtil;
import xyz.poketech.dyeityourself.util.WorldUtil;

import java.util.EnumSet;

public class EatFlowerGoal extends Goal {

    /**
     * The entity owner of this AITask
     */
    private final MobEntity flowerEaterEntity;

    /**
     * The world the flower eater entity is eating from
     */
    private final World entityWorld;

    /**
     * Number of ticks since the entity started to eat flowers
     */
    int eatingFlowerTimer;

    public EatFlowerGoal(MobEntity flowerEaterEntityIn) {
        this.flowerEaterEntity = flowerEaterEntityIn;
        this.entityWorld = flowerEaterEntityIn.world;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.flowerEaterEntity.getRNG().nextInt(this.flowerEaterEntity.isChild() ? 50 : 1000) == 0 && WorldUtil.isEntityOnFlower(this.flowerEaterEntity);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.eatingFlowerTimer = 40;
        this.entityWorld.setEntityState(this.flowerEaterEntity, (byte) 10);
        this.flowerEaterEntity.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        this.eatingFlowerTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return this.eatingFlowerTimer > 0;
    }

    /**
     * Number of ticks since the entity started to eat flowers
     */
    public int getEatingFlowerTimer() {
        return this.eatingFlowerTimer;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        this.eatingFlowerTimer = Math.max(0, this.eatingFlowerTimer - 1);

        if (this.eatingFlowerTimer == 4) {
            BlockPos blockpos = getBlockPos();

            if (WorldUtil.isEntityOnFlower(this.flowerEaterEntity)) {

                DyeColor color = DyeUtil.getDyeForFlowerAt(this.entityWorld, blockpos);
                if (this.entityWorld.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
                    this.entityWorld.destroyBlock(blockpos, false);
                }

                this.flowerEaterEntity.eatGrassBonus();

                if (this.flowerEaterEntity instanceof SheepEntity && DyeItYourself.CONFIG.sheepAbsorbColor.get()) {
                    SheepEntity sheep = (SheepEntity) this.flowerEaterEntity;
                    sheep.setFleeceColor(color);
                }
            }
        }
    }

    private BlockPos getBlockPos() {
        return new BlockPos(this.flowerEaterEntity.getPosX(), this.flowerEaterEntity.getPosY(), this.flowerEaterEntity.getPosZ());
    }

}
