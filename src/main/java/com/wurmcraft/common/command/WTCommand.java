package com.wurmcraft.common.command;

import com.wurmcraft.script.utils.StackHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class WTCommand extends CommandBase {

	@Override
	public String getName () {
		return "wt";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return null;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) throws CommandException {
		if (args.length == 1 && args[0].equalsIgnoreCase ("hand")) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				if (player.getHeldItemMainhand () != ItemStack.EMPTY) {
					String stack = new StackHelper (Thread.currentThread (),true).convert (player.getHeldItemMainhand ());
					player.sendMessage (new TextComponentString (TextFormatting.RED + stack));
				} else
					player.sendMessage (new TextComponentString (TextFormatting.RED + "Empty Hand"));
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase ("load")) {
			if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				String item = args[1];
				ItemStack itemStack = new StackHelper (Thread.currentThread (),true).convert (item);
				player.inventory.addItemStackToInventory (itemStack);
				player.sendMessage (new TextComponentString (TextFormatting.RED + "Added / Loaded!"));
			}
		}
	}
}
