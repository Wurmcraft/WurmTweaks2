package com.wurmcraft.wurmtweaks.client;

import com.wurmcraft.wurmtweaks.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit () {
		super.preInit ();
	}

	@Override
	public void init () {
		super.init ();
	}

	@Override
	public void postInit () {
		super.postInit ();
	}

	@Override
	public IThreadListener getThreadListener (MessageContext ctx) {
		if (ctx.side.isClient ())
			return Minecraft.getMinecraft ();
		return null;
	}

	@Override
	public EntityPlayer getPlayer (MessageContext ctx) {
		if (ctx.side.isClient ())
			return Minecraft.getMinecraft ().player;
		else
			return null;
	}
}
