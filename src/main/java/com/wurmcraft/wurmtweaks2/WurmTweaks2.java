package com.wurmcraft.wurmtweaks2;

import com.wurmcraft.wurmtweaks2.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks2.common.command.InterpreterCommand;
import com.wurmcraft.wurmtweaks2.common.command.WurmTweaksCommand;
import com.wurmcraft.wurmtweaks2.common.loader.ConversionHandler;
import com.wurmcraft.wurmtweaks2.common.reference.Global;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Global.MODID, name = Global.NAME)
public class WurmTweaks2 {

  public static final Logger LOGGER = LogManager.getLogger(Global.NAME);

  @EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    LOGGER.info("PreInit has started");
    WurmTweaks2API.dataConverters = ConversionHandler.setupConversions();
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
    LOGGER.info("Init has started");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent e) {
    LOGGER.info("PostInit has started");
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent e) {
    e.registerServerCommand(new WurmTweaksCommand());
    e.registerServerCommand(new InterpreterCommand());
  }
}
