package com.wurmcraft.wurmtweaks.common.event;


import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;

public class CraftEvent {

	public static HashMap <ItemStack, String> stagesToUnlock = new HashMap <> ();

	private static String getStage (ItemStack item) {
		if (item != ItemStack.EMPTY)
			for (ItemStack stack : stagesToUnlock.keySet ())
				if (StackHelper.isSameIgnoreSize (stack,item))
					return stagesToUnlock.get (stack);
		return "";
	}

	@SubscribeEvent
	public void onPlayerCraft (PlayerEvent.ItemCraftedEvent e) {
		String possibleStage = getStage (e.crafting);
		if (possibleStage.length () > 0) {
			PlayerDataHandler.getStageData (e.player).unlockStage (possibleStage);
			PlayerDataHandler.getStageData (e.player).setSynced (false);
			if (e.player.world.isRemote)
				e.player.sendMessage (new TextComponentString ("You have just unlocked the # stage".replaceAll ("#",possibleStage)));
		}
	}

	@SubscribeEvent
	public void onPickup (PlayerEvent.ItemPickupEvent e) {
		String possibleStage = getStage (e.getStack ());
		if (possibleStage.length () > 0) {
			PlayerDataHandler.getStageData (e.player).unlockStage (possibleStage);
			PlayerDataHandler.getStageData (e.player).setSynced (false);
			if (e.player.world.isRemote)
				e.player.sendMessage (new TextComponentString ("You have just unlocked the # stage".replaceAll ("#",possibleStage)));
		}
	}
}
