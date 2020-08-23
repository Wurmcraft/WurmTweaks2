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

  @SubscribeEvent
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Global.MODID)) {
      ConfigManager.load(Global.MODID, Config.Type.INSTANCE);
    }
  }
}
