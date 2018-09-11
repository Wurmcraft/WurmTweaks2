package com.wurmcraft;

import com.wurmcraft.api.WurmTweak2API;
import com.wurmcraft.api.script.anotations.DataConverter;
import com.wurmcraft.api.script.converter.IDataConverter;
import com.wurmcraft.common.CommonProxy;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.blocks.WurmTweaksBlocks;
import com.wurmcraft.common.command.WTCommand;
import com.wurmcraft.common.events.DamageHealEvents;
import com.wurmcraft.common.events.LootDestroyer;
import com.wurmcraft.common.items.WurmTweaksItems;
import com.wurmcraft.common.network.NetworkHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.common.script.FunctionBuilder;
import com.wurmcraft.common.script.ScriptChecker;
import com.wurmcraft.common.script.ScriptExecutor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = Global.MODID,
    name = Global.NAME,
    version = Global.VERSION,
    dependencies = Global.DEPENDENCIES
)
public class WurmTweaks {

  @Mod.Instance(Global.MODID)
  public static WurmTweaks instance;

  @SidedProxy(serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
  public static CommonProxy proxy;

  public static Logger logger;

  @Mod.EventHandler
  public void onPreInit(FMLPreInitializationEvent e) {
    logger = e.getModLog();
    NetworkHandler.registerPackets();
    WurmTweaksItems.register();
    WurmTweaksBlocks.register();
    proxy.preInit();
    ScriptExecutor.functions = FunctionBuilder.init(e.getAsmData());
    ScriptChecker.downloadScripts(ScriptChecker.getLoadedScriptsFromMaster());
    registerConverters(e.getAsmData());
    FunctionBuilder.preInitSupport();
    if (ConfigHandler.removeAllLoot) {
      MinecraftForge.EVENT_BUS.register(new LootDestroyer());
    }
    MinecraftForge.EVENT_BUS.register(new DamageHealEvents());
  }

  @Mod.EventHandler
  public void onInit(FMLInitializationEvent e) {
    proxy.init();
    FunctionBuilder.initSupport();
    ScriptExecutor.runScripts();
  }

  @Mod.EventHandler
  public void onPostInit(FMLPostInitializationEvent e) {
    proxy.postInit();
    ScriptExecutor.waitTillScriptsFinish();
    FunctionBuilder.postInitFinalizeSupport();
  }

  @Mod.EventHandler
  public void onServerStarting(FMLServerStartingEvent e) {
    e.registerServerCommand(new WTCommand());
    FunctionBuilder.serverStartingFinalizeSupport();
  }

  private void registerConverters(ASMDataTable table) {
    for (ASMData data : table.getAll(DataConverter.class.getName())) {
      try {
        Object instance = Class.forName(data.getClassName()).newInstance();
        if (instance instanceof IDataConverter) {
          WurmTweak2API.dataConverters.add((IDataConverter) instance);
        }
      } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
