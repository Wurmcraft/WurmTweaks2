package com.wurmcraft.common.network.server;

import com.wurmcraft.common.network.CustomMessage;
import com.wurmcraft.common.script.ScriptExecutor;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;

public class ReloadMessage extends CustomMessage.CustomClientMessage<ReloadMessage> {

  private boolean reload;

  public ReloadMessage() {
  }

  public ReloadMessage(boolean reload) {
    this.reload = reload;
  }

  @Override
  protected void read(PacketBuffer buff) throws IOException {
    reload = buff.readBoolean();
  }

  @Override
  protected void write(PacketBuffer buff) {
    buff.writeBoolean(reload);
  }

  @Override
  public void process(EntityPlayer player, Side side) {
    ScriptExecutor.reload(true);
    player.sendMessage(
        new TextComponentString(TextFormatting.RED + "Reloading Scripts... (Please wait)"));
  }
}
