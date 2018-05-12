package com.wurmcraft.script.support;

import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class OreStages extends SupportHelper {

	public static HashMap <ItemStack, String> stagesCache = new HashMap<> ();
	private List<Object[]> stagesToUnlock = Collections.synchronizedList (new ArrayList<> ());

	public OreStages () {
		super ("orestages");
	}

	private static String getStage (ItemStack item) {
		if (item != ItemStack.EMPTY)
			for (ItemStack stack : stagesCache.keySet ())
				if (isSameIgnoreSize (stack,item))
					return stagesCache.get (stack);
		return "";
	}

	private static boolean isSameIgnoreSize (ItemStack a,ItemStack b) {
		return a.getItem ().equals (b.getItem ()) && a.getTagCompound () == b.getTagCompound () && ((a.getItemDamage () == b.getItemDamage ()) || a.getItemDamage () == Short.MAX_VALUE);
	}

	@SubscribeEvent
	public void onPlayerCraft (PlayerEvent.ItemCraftedEvent e) {
		String possibleStage = getStage (e.crafting);
		if (possibleStage.length () > 0) {
			GameStageHelper.getPlayerData (e.player).addStage (possibleStage);
			if (!GameStageHelper.getPlayerData (e.player).hasStage (possibleStage)) {
				if (e.player.world.isRemote)
					e.player.sendMessage (new TextComponentString (TextFormatting.AQUA + "You have just unlocked the # stage".replaceAll ("#",possibleStage)));
				if (e.player instanceof EntityPlayerMP)
					GameStageHelper.syncPlayer ((EntityPlayerMP) e.player);
			}
		}
	}

	@ScriptFunction
	public void onItemPickup (PlayerEvent.ItemPickupEvent e) {
		String possibleStage = getStage (e.getStack ());
		if (possibleStage.length () > 0) {
			GameStageHelper.getPlayerData (e.player).addStage (possibleStage);
			if (!GameStageHelper.getPlayerData (e.player).hasStage (possibleStage)) {
				if (e.player.world.isRemote)
					e.player.sendMessage (new TextComponentString (TextFormatting.AQUA + "You have just unlocked the # stage".replaceAll ("#",possibleStage)));
				if (e.player instanceof EntityPlayerMP)
					GameStageHelper.syncPlayer ((EntityPlayerMP) e.player);
			}
		}
	}

	@Override
	public void init () {
		stagesCache.clear ();
		stagesToUnlock.clear ();
		MinecraftForge.EVENT_BUS.register (this);
	}

	@Override
	public void finishSupport () {
		stagesCache.clear ();
		stagesToUnlock.clear ();
		for (Object[] items : stagesToUnlock)
			stagesCache.put ((ItemStack) items[0],(String) items[1]);
	}

	@ScriptFunction
	public void addOreStage (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 3,"addOreStage('<stage> <block> <replacment>')");
		String stage = input[0];
		isValid (helper,input[1],input[2]);
		ItemStack block = convertStack (helper,input[1]);
		ItemStack replacment = convertStack (helper,input[2]);
		OreTiersAPI.addReplacement (stage,Block.getBlockFromItem (block.getItem ()),block.getItemDamage (),Block.getBlockFromItem (replacment.getItem ()),replacment.getItemDamage (),true);
	}

	@ScriptFunction
	public void addUnlockCraft (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 1,"addUnlockCraft('<item> <stageName>')");
		isValid (helper,input[0]);
		stagesToUnlock.add (new Object[] {convertStack (helper,input[0]),input[1]});
	}
}
