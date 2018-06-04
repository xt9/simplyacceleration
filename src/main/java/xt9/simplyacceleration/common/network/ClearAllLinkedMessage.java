package xt9.simplyacceleration.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xt9.simplyacceleration.common.inventory.ContainerAccelerator;

/**
 * Created by xt9 on 2018-06-01.
 */
public class ClearAllLinkedMessage implements IMessage {
    public ClearAllLinkedMessage() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<ClearAllLinkedMessage, IMessage> {

        @Override
        public IMessage onMessage(ClearAllLinkedMessage message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() -> {
                if(player.openContainer instanceof ContainerAccelerator) {
                    ContainerAccelerator container = (ContainerAccelerator) player.openContainer;
                    container.tile.clearLinked();
                }
            });
            return null;
        }
    }
}
