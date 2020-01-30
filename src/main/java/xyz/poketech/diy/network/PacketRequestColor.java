package xyz.poketech.diy.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import xyz.poketech.diy.DyeItYourself;
import xyz.poketech.diy.util.color.NBTColorUtil;

import java.util.function.Supplier;

public class PacketRequestColor {

    private int entityID;

    public PacketRequestColor() {
    }

    public PacketRequestColor(int entityID) {
        this.entityID = entityID;
    }

    PacketRequestColor(Entity entity) {
        this.entityID = entity.getEntityId();
    }

    public PacketRequestColor (PacketBuffer buf) {
        this(buf.readInt());
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(entityID);
    }

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER && serverPlayer != null) {
                Entity entity = serverPlayer.getServerWorld().getEntityByID(this.entityID);
                if (entity != null && entity.getPersistentData().contains(NBTColorUtil.COLOR_KEY)) {
                    int color = entity.getPersistentData().getInt(NBTColorUtil.COLOR_KEY);
                    DyeItYourself.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new PacketUpdateColor(this.entityID, color));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

