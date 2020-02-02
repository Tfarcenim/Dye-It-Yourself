package xyz.poketech.diy;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {

    final public ConfigHelper.ConfigValueListener<Boolean> doDropDye;

    final public ConfigHelper.ConfigValueListener<Integer> rngUpperBoundTime;

    final public ConfigHelper.ConfigValueListener<Integer> rngLowerBoundTime;

    final public ConfigHelper.ConfigValueListener<Integer> maxDyeDrop;

    final public ConfigHelper.ConfigValueListener<Integer> minDyeDrop;

    final public ConfigHelper.ConfigValueListener<Boolean> sheepEatFlowers;

    final public ConfigHelper.ConfigValueListener<Boolean> sheepAbsorbColor;

    public ConfigHandler(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
        builder.push("general");
        sheepEatFlowers = subscriber.subscribe(builder
                .comment("Sheep eat flowers")
                .translation("config.diy.sheep_eat_flowers")
                .define("sheepEatFlowers", true)
        );
        sheepAbsorbColor = subscriber.subscribe(builder
                .comment("Sheep take the color of flowers they eat")
                .translation("config.diy.sheep_absorb")
                .define("sheepAbsorbColor", true)
        );
        builder.push("dyeDropping");
        doDropDye = subscriber.subscribe(builder
                .comment("Sheep drop dye")
                .translation("config.diy.sheep_drop_dye")
                .define("dropDye", true)
        );
        rngUpperBoundTime = subscriber.subscribe(builder
                .comment("Max time between dye drops")
                .translation("config.diy.sheep_drop_time_higher")
                .define("maxDropTime", 12000)
        );
        rngLowerBoundTime = subscriber.subscribe(builder
                .comment("Min time between dye drops")
                .translation("config.diy.sheep_drop_time_lower")
                .define("minDropTime", 6000)
        );
        maxDyeDrop = subscriber.subscribe(builder
                .comment("Max dye dropped at once")
                .translation("config.diy.max_dye_drop")
                .defineInRange("maxDyeDrop", 2, 0, Integer.MAX_VALUE)
        );
        minDyeDrop = subscriber.subscribe(builder
                .comment("Min dye dropped at once")
                .translation("config.diy.min_dye_drop")
                .defineInRange("minDyeDrop", 1, 0, Integer.MAX_VALUE)
        );
        builder.pop();
    }
}
