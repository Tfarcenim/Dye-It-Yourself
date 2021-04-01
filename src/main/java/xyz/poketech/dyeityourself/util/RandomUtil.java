package xyz.poketech.dyeityourself.util;

import org.apache.commons.lang3.RandomUtils;
import xyz.poketech.dyeityourself.DyeItYourself;

public class RandomUtil {

    /**
     * @return a time in tick until the next dye drop based on the config
     */
    public static int getNextDye() {
        int min = DyeItYourself.CONFIG.rngLowerBoundTime.get();
        int max = DyeItYourself.CONFIG.rngUpperBoundTime.get();

        if(min > max || max - min < 0) {
            DyeItYourself.LOGGER.error("Tried to get a random next dye time but min > max, min = " + min + " max = " + max);
            return 0;
        }
        return RandomUtils.nextInt(min, max);
    }

    public static int getDyeDropAmountSafe() {
        int min =DyeItYourself.CONFIG.minDyeDrop.get();
        int max = DyeItYourself.CONFIG.maxDyeDrop.get();

        if(min > max || max - min < 0) {
            DyeItYourself.LOGGER.error("Tried to get a random amount of dye but min > max, min = " + min + " max = " + max);
            return 0;
        }
        return RandomUtils.nextInt(min, max + 1);
    }
}
