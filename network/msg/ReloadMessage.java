package com.wurmcraft.common.network.msg;

import com.wurmcraft.common.network.CustomMessage;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;


import java.io.IOException;

// TODO Add to command
public class ReloadMessage extends CustomMessage.CustomClientMessage <ReloadMessage> {

	private boolean reload;

	public ReloadMessage () {
	}

	public ReloadMessage (boolean reload) {
		this.reload = reload;
	}

	@Override
	protected void read (PacketBuffer buff) throws IOException {
		reload = buff.readBoolean ();
	}

	@Override
	protected void write (PacketBuffer buff) {
		buff.writeBoolean (reload);
	}

	@Override
	public void process (EntityPlayer player,Side side) {
		// TODO Reload the Scripts
	}
}
