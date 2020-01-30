package xyz.poketech.diy.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.PacketDistributor;
import xyz.poketech.diy.DyeItYourself;

public class PacketHandler {

    public static void registerMessages() {
        int id = 0;

        DyeItYourself.NETWORK.registerMessage(
                id++,
                PacketRequestColor.class,
                PacketRequestColor::encode,
                PacketRequestColor::new,
                PacketRequestColor::onMessage
        );
        DyeItYourself.NETWORK.registerMessage(
                id++,
                PacketUpdateColor.class,
                PacketUpdateColor::encode,
                PacketUpdateColor::new,
                PacketUpdateColor::onMessage
        );
    }

    public static void sendColorUpdate(int target, int color, BlockPos pos, DimensionType dimension, int range) {
        DyeItYourself.NETWORK.send(
                PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), range, dimension)),
                new PacketUpdateColor(target,  color)
        );
    }
}
