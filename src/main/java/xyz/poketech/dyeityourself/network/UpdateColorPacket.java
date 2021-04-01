package xyz.poketech.dyeityourself.network;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateColorPacket {

    private int entityId;
    private int color;

    public UpdateColorPacket() {
    }

    public UpdateColorPacket(Entity entityIn, int color) {
        this(entityIn.getEntityId(), color);
    }

    public UpdateColorPacket(int entityId, int color) {
        this.entityId = entityId;
        this.color = color;
    }

    public UpdateColorPacket(PacketBuffer buf) {
        this(buf.readInt(), buf.readInt());
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.color);
    }


    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    public int getColor() {
        return this.color;
    }

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(
                Dist.CLIENT, () -> () -> DistHelper.updateSheepColor(this.entityId, this.color)
        ));
        ctx.get().setPacketHandled(true);
    }

}
