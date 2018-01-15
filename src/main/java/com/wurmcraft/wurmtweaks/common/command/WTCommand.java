package com.wurmcraft.wurmtweaks.common.command;

import com.wurmcraft.wurmtweaks.WurmTweaks;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Local;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

public class WTCommand extends CommandBase {

	@Override
	public String getName () {
		return "wt";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/wt hand";
	}

	@Override
	public List <String> getAliases () {
		List <String> aliases = new ArrayList <> ();
		aliases.add ("wurmtweaks");
		aliases.add ("WurmTweaks");
		aliases.add ("wurmtweaks2");
		aliases.add ("WurmTweaks2");
		aliases.add ("WT2");
		return aliases;
	}

	@Override
	public boolean checkPermission (MinecraftServer server,ICommandSender sender) {
		return true;
	}

	@Override
	public void execute (MinecraftServer server,ICommandSender sender,String[] args) {
		if (args.length > 0 && !sender.getEntityWorld ().isRemote) {
			if (args[0].equalsIgnoreCase ("hand"))
				hand (sender);
			else if (args[0].equalsIgnoreCase ("reload")) {
				WurmTweaks.dl.init ();
				sender.sendMessage (new TextComponentString (TextFormatting.RED + "Reloaded! (Old Recipes Are Not Removed)"));
			}
		} else
			sender.sendMessage (new TextComponentString (TextFormatting.GOLD + getUsage (sender)));
	}

	private void hand (ICommandSender sender) {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player.getHeldItemMainhand () != ItemStack.EMPTY) {
				String item = StackHelper.convert (player.getHeldItemMainhand ());
				player.sendMessage (new TextComponentString (TextFormatting.GOLD + I18n.translateToLocal (Local.HELD_ITEM).replaceAll ("%ITEM%",TextFormatting.LIGHT_PURPLE + item).replaceAll ("'",TextFormatting.RED + "'")));
				if (ConfigHandler.copyItemName)
					addToClipboard (item);
			} else
				sender.sendMessage (new TextComponentString (TextFormatting.RED + I18n.translateToLocal (Local.EMPTY_HAND)));
		} else
			sender.sendMessage (new TextComponentString (TextFormatting.RED + I18n.translateToLocal (Local.REQUIRES_PLAYER)));
	}

	private void addToClipboard (String text) {
		StringSelection sel = new StringSelection (text);
		Clipboard board = Toolkit.getDefaultToolkit ().getSystemClipboard ();
		board.setContents (sel,sel);
	}
}
