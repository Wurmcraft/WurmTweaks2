package com.wurmcraft.wurmtweaks2.common.config;

import com.wurmcraft.wurmtweaks2.common.reference.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Global.MODID)
@Config(modid = Global.MODID)
public class ConfigHandler {

  @Config.Comment("Debug mode")
  public static boolean debug = false;

  @Config.Comment("Caches Conversions, Improves Performance, but increases memory usage")
  public static boolean cacheConversions = true;

  @Config.Comment("Script to run on minecraft startup, can be a a single python script or a .ws script with multiple scripts linked / downloaded, see wiki for more info")
  public static String initialScript = "https://raw.githubusercontent.com/Wurmcraft/WurmTweaks2/1.12.2/scripts/bootstrap.ws";

  @SubscribeEvent
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Global.MODID)) {
      ConfigManager.load(Global.MODID, Config.Type.INSTANCE);
    }
  }
}
