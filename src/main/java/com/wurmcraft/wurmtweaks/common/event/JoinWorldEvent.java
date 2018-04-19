package com.wurmcraft.wurmtweaks.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.io.IOException;

public class JoinWorldEvent {

	private static File SAVE_LOC = null;

	private static boolean firstJoin (EntityPlayer player) {
		File file = new File (SAVE_LOC + File.separator + player.getGameProfile ().getId ().toString ());
		return !file.exists ();
	}

	@SubscribeEvent
	public void onPlayerJoin (PlayerEvent.PlayerLoggedInEvent e) {
		if (SAVE_LOC == null)
			SAVE_LOC = new File (e.player.world.getSaveHandler ().getWorldDirectory () + File.separator + "firstJoin");
		if (firstJoin (e.player)) {
			File file = new File (SAVE_LOC + File.separator + e.player.getGameProfile ().getId ().toString ());
			if (!SAVE_LOC.exists ())
				SAVE_LOC.mkdirs ();
			try {
				file.createNewFile ();
			} catch (IOException f) {
				f.printStackTrace ();
			}
		}
		e.player.heal (Integer.MAX_VALUE);
	}
}
