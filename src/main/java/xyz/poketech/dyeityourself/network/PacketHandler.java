package xyz.poketech.dyeityourself.network;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import xyz.poketech.dyeityourself.DyeItYourself;

public class PacketHandler {

    public static void registerMessages() {
        int id = 0;

        DyeItYourself.NETWORK.registerMessage(
                id++,
                RequestColorPacket.class,
                RequestColorPacket::encode,
                RequestColorPacket::new,
                RequestColorPacket::onMessage
        );
        DyeItYourself.NETWORK.registerMessage(
                id++,
                UpdateColorPacket.class,
                UpdateColorPacket::encode,
                UpdateColorPacket::new,
                UpdateColorPacket::onMessage
        );
    }

    public static void sendColorUpdate(int target, int color, BlockPos pos, RegistryKey<World> dimension, int range) {
        DyeItYourself.NETWORK.send(
                PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), range, dimension)),
                new UpdateColorPacket(target,  color)
        );
    }
}
