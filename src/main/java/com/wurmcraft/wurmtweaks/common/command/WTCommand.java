package com.wurmcraft.wurmtweaks.common.command;

import com.wurmcraft.wurmtweaks.WurmTweaks;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.common.network.NetworkHandler;
import com.wurmcraft.wurmtweaks.common.network.msg.CopyMessage;
import com.wurmcraft.wurmtweaks.reference.Local;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class WTCommand extends CommandBase {

	@Override
	public String getName () {
		return "wt";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/wt <hand | reload | load> <data>";
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
				WurmTweaks.dl.reload ();
				sender.sendMessage (new TextComponentString (TextFormatting.RED + "Reloaded! (Old Machine Recipes Are Not Removed!)"));
			} else if (args[0].equalsIgnoreCase ("load") && sender.getCommandSenderEntity () instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
				if (args.length == 2) {
					ItemStack stack = StackHelper.convert (args[1]);
					if (stack != ItemStack.EMPTY) {
						player.addItemStackToInventory (stack);
					} else
						sender.sendMessage (new TextComponentString ("Invalid Stack '" + args[1] + "'"));
				} else
					sender.sendMessage (new TextComponentString ("/wt load <item>"));
			} else if (args[0].equalsIgnoreCase ("exportInv") && sender.getCommandSenderEntity () instanceof EntityPlayer) {
				exportInv (sender);
			}
		} else
			sender.sendMessage (new TextComponentString (TextFormatting.GOLD + getUsage (sender)));
	}

	private void hand (ICommandSender sender) {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (player.getHeldItemMainhand () != ItemStack.EMPTY) {
				String item = StackHelper.convert (player.getHeldItemMainhand (),0).replaceAll (" ","%");
				List <String> oreEntrys = new ArrayList <> ();
				if (!player.getHeldItemMainhand ().isEmpty ())
					for (int id : OreDictionary.getOreIDs (player.getHeldItemMainhand ()))
						oreEntrys.add (OreDictionary.getOreName (id));
				player.sendMessage (new TextComponentString (TextFormatting.GOLD + I18n.translateToLocal (Local.HELD_ITEM).replaceAll ("%ITEM%",TextFormatting.LIGHT_PURPLE + item).replaceAll ("'",TextFormatting.RED + "'")));
				if (player.getHeldItemMainhand ().getItem () instanceof ItemBucket || player.getHeldItemMainhand ().getItem () == ForgeModContainer.getInstance ().universalBucket && player.getHeldItemMainhand ().hasTagCompound ())
					oreEntrys.add (StackHelper.convert (player.getHeldItemMainhand (),1));
				if (!player.getHeldItemMainhand ().isEmpty ())
					player.sendMessage (new TextComponentString (TextFormatting.GOLD + I18n.translateToLocal (Local.HELD_ITEM).replaceAll ("%ITEM%",TextFormatting.LIGHT_PURPLE + Strings.join (oreEntrys,",")).replaceAll ("'",TextFormatting.RED + "'")));
				if (ConfigHandler.copyItemName)
					NetworkHandler.sendTo (new CopyMessage (item),(EntityPlayerMP) player);
			} else
				sender.sendMessage (new TextComponentString (TextFormatting.RED + I18n.translateToLocal (Local.EMPTY_HAND)));
		} else
			sender.sendMessage (new TextComponentString (TextFormatting.RED + I18n.translateToLocal (Local.REQUIRES_PLAYER)));
	}

	private void exportInv (ICommandSender sender) {
		if (sender.getCommandSenderEntity () instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity ();
			if (!player.inventory.isEmpty ()) {
				StringBuilder items = new StringBuilder ();
				for (ItemStack inv : player.inventory.mainInventory)
					items.append (getName (inv) + "\n");
				if (ConfigHandler.copyItemName)
					NetworkHandler.sendTo (new CopyMessage (items.toString ()),(EntityPlayerMP) player);
			}
		} else
			sender.sendMessage (new TextComponentString (TextFormatting.RED + I18n.translateToLocal (Local.REQUIRES_PLAYER)));
	}

	private String getName (ItemStack stack) {
		if (stack.getItem () instanceof ItemBucket || stack.getItem () == ForgeModContainer.getInstance ().universalBucket && stack.hasTagCompound ())
			return StackHelper.convert (stack,1);
		else
			return StackHelper.convert (stack,0);
	}
}
