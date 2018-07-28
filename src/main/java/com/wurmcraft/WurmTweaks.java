package com.wurmcraft;

import com.wurmcraft.common.CommonProxy;
import com.wurmcraft.common.blocks.WurmTweaksBlocks;
import com.wurmcraft.common.command.WTCommand;
import com.wurmcraft.common.items.WurmTweaksItems;
import com.wurmcraft.common.network.NetworkHandler;
import com.wurmcraft.common.reference.Global;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPENDENCIES)
public class WurmTweaks {

  @Mod.Instance(Global.MODID)
  public static WurmTweaks instance;

  @SidedProxy(serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
  public static CommonProxy proxy;

  @Mod.EventHandler
  public void onPreInit(FMLPreInitializationEvent e) {
    NetworkHandler.registerPackets();
    WurmTweaksItems.register();
    WurmTweaksBlocks.register();
    proxy.preInit();
  }

  @Mod.EventHandler
  public void onInit(FMLInitializationEvent e) {
    proxy.init();
  }

  @Mod.EventHandler
  public void onPostInit(FMLPostInitializationEvent e) {
    proxy.postInit();
  }

  @Mod.EventHandler
  public void onServerStarting(FMLServerStartingEvent e) {
    e.registerServerCommand(new WTCommand());
  }
}
