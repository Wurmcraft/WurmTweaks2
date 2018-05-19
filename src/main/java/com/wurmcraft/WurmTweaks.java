package com.wurmcraft;

import com.wurmcraft.common.CommonProxy;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.blocks.WurmTweaksBlocks;
import com.wurmcraft.common.command.WTCommand;
import com.wurmcraft.common.items.WurmTweaksItems;
import com.wurmcraft.common.network.NetworkHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.FunctionsRegistry;
import com.wurmcraft.script.WurmScript;
import com.wurmcraft.script.support.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPENDENCIES)
public class WurmTweaks {

 @Mod.Instance(Global.MODID)
 public static WurmTweaks instance;

 @SidedProxy(serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
 public static CommonProxy proxy;

 public static WurmScript script;
 public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Global.MODID);

 @Mod.EventHandler
 public void onPreInit(FMLPreInitializationEvent e) {
  NetworkHandler.registerPackets ();
  File
   logDir = new File(ConfigHandler.logDirectory),
   scriptDir = WurmScript.getFileFromName(ConfigHandler.masterScript).getParentFile();
  if (!logDir.exists()) logDir.mkdirs();
  if (!scriptDir.exists()) scriptDir.mkdirs();
  WurmTweaksItems.register();
  WurmTweaksBlocks.register();
  proxy.preInit();
  FunctionsRegistry.register(Minecraft.class);
  FunctionsRegistry.register(AbyssalCraft.class);
  FunctionsRegistry.register(ActuallyAdditions.class);
  FunctionsRegistry.register(AE2.class);
  FunctionsRegistry.register(AstralSorcery.class);
  FunctionsRegistry.register(Avaritia.class);
  FunctionsRegistry.register(BetterWithMods.class);
//  FunctionsRegistry.register(BloodMagic.class); //
//  FunctionsRegistry.register(Botania.class); //
  FunctionsRegistry.register(Calculator.class);
  FunctionsRegistry.register(CharcoalPit.class);
  FunctionsRegistry.register(DraconicEvolution.class);
//  FunctionsRegistry.register(EnvironmentalTech.class); //
  FunctionsRegistry.register(Events.class);
  FunctionsRegistry.register(ExtraUtils2.class);
  FunctionsRegistry.register(GalacticCraft.class);
  FunctionsRegistry.register(ImmersiveEngineering.class);
  FunctionsRegistry.register(IndustrialForegoing.class);
  // TODO "Recipes should be registered before PostInit. Try net.minecraftforge.event.RegistryEvent.Register<IRecipe>'
//  FunctionsRegistry.register(Mekanism.class); //
  FunctionsRegistry.register(NuclearCraft.class);
  FunctionsRegistry.register(OreStages.class);
  FunctionsRegistry.register(PneumaticCraft.class);
  FunctionsRegistry.register(SonarCore.class);
  FunctionsRegistry.register(TConstruct.class);
  FunctionsRegistry.register(TechReborn.class);
  FunctionsRegistry.register(Thaumcraft.class);
  FunctionsRegistry.register(ThermalExpansion.class);
  FunctionsRegistry.register(ToughAsNails.class);
  script = new WurmScript();
  if (ConfigHandler.checkForUpdates) WurmScript.downloadScripts();
 }

 @Mod.EventHandler
 public void onInit(FMLInitializationEvent e) {
  proxy.init();
  FunctionsRegistry.init();
 }

 public static final Runnable SCRIPT_MANAGER = () -> {
  final File logFile = new File(ConfigHandler.logDirectory + File.separator + "ScriptManager.log");
  if (!logFile.exists()) {
   try {
    logFile.createNewFile();
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
  Thread.currentThread().setName("Script Manager Thread");
  try (final PrintStream log = new PrintStream(new FileOutputStream(logFile, false))) {
   final List<Thread> scriptWorkers = new ArrayList<>();
   for (File file : WurmScript.getRunnableScripts()) {
    try {
     Thread script = new Thread(WurmScript.scriptToRunnable(file));
     script.setName(file.getName());
     scriptWorkers.add(script);
     script.start();
    } catch (Throwable t) {
     log.println("Script Manager thread encountered unexpected exception!");
     t.printStackTrace(log);
     log.println("Continuing!");
    }
   }
   //Monitor script workers until they've all finished
   boolean allFinished;
   do {
    allFinished = true;
    for (Thread worker : scriptWorkers) {
     Thread.State state = worker.getState();
     allFinished &= state == Thread.State.TERMINATED;
    }
    //.1s delay between state checks
    try {
     Thread.currentThread().sleep(100l);
    } catch (InterruptedException e1) {
     log.println("Script Manager thread unexpectedly interrupted!");
     e1.printStackTrace(log);
     log.println("Continuing!");
    }
   } while (!allFinished);
  } catch (IOException e1) {
   System.err.println("Failed to open log for script manager at: '" + logFile.getAbsolutePath() + "'!");
  } finally {
   //Stop thread
   Thread.currentThread().interrupt();
   //Notify other threads that this thread has stopped
   synchronized (Thread.currentThread()) {
    Thread.currentThread().notify();
   }
  }
 };

 @Mod.EventHandler
 public void onPostInit(FMLPostInitializationEvent e) {
  proxy.postInit();
  //Make a new instance of the SCRIPT_MANAGER thread
  Thread scriptManager = new Thread(SCRIPT_MANAGER);
  scriptManager.start();
  //Wait for SCRIPT_MANAGER to finish
  synchronized (scriptManager) {
   try {
    scriptManager.wait();
   } catch (InterruptedException e1) {
   }
  }
  //Finish loading support
  FunctionsRegistry.finishSupport();
 }

 @Mod.EventHandler
 public void onLoadComplete(FMLLoadCompleteEvent e) {

 }

 @Mod.EventHandler
 public void onServerStarting(FMLServerStartingEvent e) {
  e.registerServerCommand(new WTCommand());
 }

 @Mod.EventHandler
 public void onServerStopping(FMLServerStoppingEvent e) {

 }
}
