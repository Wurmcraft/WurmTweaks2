package com.wurmcraft.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

  public void preInit() {
    MinecraftForge.EVENT_BUS.register(new Registry());
  }

  public void init() {}

  public void postInit() {}

  public IThreadListener getThreadListener(MessageContext context) {
    if (context.side.isServer()) {
      return context.getServerHandler().player.mcServer;
    }
    return null;
  }

  public EntityPlayer getPlayer(MessageContext ctx) {
    if (ctx.side.isServer()) {
      return ctx.getServerHandler().player;
    }
    return null;
  }
}
