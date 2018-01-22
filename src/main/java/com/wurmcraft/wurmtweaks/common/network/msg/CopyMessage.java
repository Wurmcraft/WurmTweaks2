package com.wurmcraft.wurmtweaks.common.network.msg;

import com.wurmcraft.wurmtweaks.common.network.CustomMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class CopyMessage extends CustomMessage.CustomClientMessage <CopyMessage> {

	private NBTTagCompound data;

	public CopyMessage () {

	}

	public CopyMessage (String d) {
		NBTTagCompound nbt = new NBTTagCompound ();
		nbt.setString ("0",d);
		this.data = nbt;
	}

	@Override
	protected void read (PacketBuffer buff) throws IOException {
		data = buff.readCompoundTag ();
	}

	@Override
	protected void write (PacketBuffer buff) {
		buff.writeCompoundTag (data);
	}

	@Override
	public void process (EntityPlayer player,Side side) {
		StringSelection sel = new StringSelection (data.getString ("0"));
		Clipboard board = Toolkit.getDefaultToolkit ().getSystemClipboard ();
		board.setContents (sel,sel);
	}
}
