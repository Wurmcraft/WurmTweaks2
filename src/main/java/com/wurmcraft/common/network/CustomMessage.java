package com.wurmcraft.common.network;

import com.google.common.base.Throwables;
import com.wurmcraft.WurmTweaks;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class CustomMessage<T extends CustomMessage<T>>
    implements IMessage, IMessageHandler<T, IMessage> {

  private static final <T extends CustomMessage<T>> void checkAndHandle(
      CustomMessage<T> msg, MessageContext ctx) {
    IThreadListener thread = WurmTweaks.proxy.getThreadListener(ctx);
    thread.addScheduledTask(() -> msg.process(WurmTweaks.proxy.getPlayer(ctx), ctx.side));
  }

  protected abstract void read(PacketBuffer buff) throws IOException;

  protected abstract void write(PacketBuffer buff) throws IOException;

  public abstract void process(EntityPlayer player, Side side);

  protected boolean isValidOnSide(Side side) {
    return true;
  }

  protected boolean requiresMain() {
    return true;
  }

  @Override
  public void fromBytes(ByteBuf buff) {
    try {
      read(new PacketBuffer(buff));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void toBytes(ByteBuf buff) {
    try {
      write(new PacketBuffer(buff));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public final IMessage onMessage(T msg, MessageContext ctx) {
    if (!msg.isValidOnSide(ctx.side)) {
      throw new RuntimeException(
          "Invalid Side " + ctx.side.name() + " for " + msg.getClass().getSimpleName());
    } else if (msg.requiresMain()) {
      checkAndHandle(msg, ctx);
    } else {
      msg.process(WurmTweaks.proxy.getPlayer(ctx), ctx.side);
    }
    return null;
  }

  public abstract static class CustomClientMessage<T extends CustomMessage<T>>
      extends CustomMessage<T> {

    @Override
    protected final boolean isValidOnSide(Side side) {
      return side.isClient();
    }
  }

  public abstract static class CustomServerMessage<T extends CustomMessage<T>>
      extends CustomMessage<T> {

    @Override
    protected final boolean isValidOnSide(Side side) {
      return side.isServer();
    }
  }
}
