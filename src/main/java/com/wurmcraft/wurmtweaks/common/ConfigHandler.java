package com.wurmcraft.wurmtweaks.common;

import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.reference.Local;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config (modid = Global.MODID)
public class ConfigHandler {

	@Config.Comment ("Enable / Disable Debug Mode")
	@Config.LangKey (Local.CONFIG_DEBUG)
	public static boolean debug = false;

	@SubscribeEvent
	public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID ().equals (Global.MODID)) {
			ConfigManager.load (Global.MODID,Config.Type.INSTANCE);
		}
	}

}
