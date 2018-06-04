package xt9.simplyacceleration.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xt9.simplyacceleration.SimplyConfig;
import xt9.simplyacceleration.common.inventory.ContainerAccelerator;
import xt9.simplyacceleration.common.tiles.TileEntityAccelerator;
import xt9.simplyacceleration.common.utils.MathHelper;

/**
 * Created by xt9 on 2018-05-28.
 */
public class UpdateAcceleratorTickMessage implements IMessage {
    private int tickMultiplier;

    @SuppressWarnings("unused")
    public UpdateAcceleratorTickMessage() {}

    public UpdateAcceleratorTickMessage(int tickMultiplier) {
        this.tickMultiplier = tickMultiplier;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tickMultiplier = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tickMultiplier);
    }

    public static class Handler implements IMessageHandler<UpdateAcceleratorTickMessage, IMessage> {

        @Override
        public IMessage onMessage(UpdateAcceleratorTickMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() -> {
                if(player.openContainer instanceof ContainerAccelerator) {
                    ContainerAccelerator container = (ContainerAccelerator) player.openContainer;
                    container.tile.setTickMultiplier(MathHelper.ensureRange(message.tickMultiplier, 1, SimplyConfig.getMaxSpeedMultiplier()));
                }
            });
            return null;
        }
    }

}
