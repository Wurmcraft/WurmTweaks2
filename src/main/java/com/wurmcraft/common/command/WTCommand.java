package com.wurmcraft.common.command;

import com.wurmcraft.common.network.NetworkHandler;
import com.wurmcraft.common.network.server.CopyMessage;
import com.wurmcraft.common.network.server.ReloadMessage;
import com.wurmcraft.common.script.ScriptExecutor;
import com.wurmcraft.common.support.utils.Converter;
import java.util.ArrayList;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

// TODO Build Command
// TODO Rework Command
public class WTCommand extends CommandBase {

  @Override
  public String getName() {
    return "wt";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(TextFormatting.RED + getUsage(sender)));
    } else if (args.length == 1 && args[0].equalsIgnoreCase("hand")) {
      hand(args, sender);
    } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
      reload(args, sender);
    }
  }

  private void hand(String[] args, ICommandSender sender) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
        String hand = Converter.getFromName("ItemStack").toString(player.getHeldItemMainhand());
        sender.sendMessage(new TextComponentString(TextFormatting.AQUA + "Item: " + hand));
        List<String> ores = new ArrayList<>();
        for (int id : OreDictionary.getOreIDs(player.getHeldItemMainhand())) {
          ores.add(OreDictionary.getOreName(id));
        }
        sender.sendMessage(
            new TextComponentString(TextFormatting.AQUA + "Ore: " + Strings.join(ores, ", ")));
        if (!player.world.isRemote) {
          NetworkHandler.sendTo(new CopyMessage(hand), (EntityPlayerMP) player);
        }
      } else {
        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Empty Hand!"));
      }
    } else {
      sender.sendMessage(new TextComponentString(TextFormatting.RED + "Players Only"));
    }
  }

  private void reload(String[] args, ICommandSender sender) {
    // TODO Reload JEI Recipes
    ScriptExecutor.reload(true);
    NetworkHandler.sendToAll(new ReloadMessage(true));
  }
}
