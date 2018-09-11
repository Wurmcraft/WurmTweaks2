package com.wurmcraft.common.network.server;

import com.wurmcraft.common.network.CustomMessage;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class CopyMessage extends CustomMessage.CustomClientMessage<CopyMessage> {

  private NBTTagCompound data;

  public CopyMessage() {}

  public CopyMessage(String d) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setString("0", d);
    this.data = nbt;
  }

  @Override
  protected void read(PacketBuffer buff) throws IOException {
    data = buff.readCompoundTag();
  }

  @Override
  protected void write(PacketBuffer buff) {
    buff.writeCompoundTag(data);
  }

  @Override
  public void process(EntityPlayer player, Side side) {
    StringSelection sel = new StringSelection(data.getString("0"));
    Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
    board.setContents(sel, sel);
  }
}
