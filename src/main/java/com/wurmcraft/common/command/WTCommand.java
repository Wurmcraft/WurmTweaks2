package com.wurmcraft.common.command;

import com.wurmcraft.WurmTweaks;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.script.FunctionsRegistry;
import com.wurmcraft.script.WurmScript;
import com.wurmcraft.script.utils.StackHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class WTCommand extends CommandBase {

 @Override
 public String getName() {
  return "wt";
 }

 @Override
 public String getUsage(ICommandSender sender) {
  return null;
 }

 @Override
 public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
  Entity sendingEntity = sender.getCommandSenderEntity();
  final EntityPlayer
   player = (sendingEntity instanceof EntityPlayer) ? (EntityPlayer)sendingEntity : null;
  if (args.length == 1) {
   if (args[0].equalsIgnoreCase("hand")) {
    if (player != null) {
     if (player.getHeldItemMainhand() != ItemStack.EMPTY) {
      String stack = new StackHelper(true, System.out).convert(player.getHeldItemMainhand());
      player.sendMessage(new TextComponentString(TextFormatting.RED + stack));
     } else {
      player.sendMessage(new TextComponentString(TextFormatting.RED + "Empty Hand"));
     }
    }
   } else if (args[0].equalsIgnoreCase("reload")) {
    Thread scriptManager = new Thread(() -> {
     Thread.currentThread().setName("WurmTweaks Reload Recipes");
     if (ConfigHandler.checkForUpdates) {
      if (WurmScript.downloadScripts()) {
       FunctionsRegistry.loadedSupport.clear();
       if (player != null) {
        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Downloaded Scripts!"));
       }
      }
     }
     WurmTweaks.SCRIPT_MANAGER.run();
     FunctionsRegistry.finishSupport();
     if (player != null) {
      player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Reload complete!"));
     }
     Thread.currentThread().interrupt();
    });
    scriptManager.start();
   }
  } else if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
   if (player != null) {
    String item = args[1];
    ItemStack itemStack = new StackHelper(true, System.out).convert(item);
    player.inventory.addItemStackToInventory(itemStack);
    player.sendMessage(new TextComponentString(TextFormatting.RED + "Added / Loaded!"));
   }
  }
 }
}
