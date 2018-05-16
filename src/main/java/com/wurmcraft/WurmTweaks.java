package com.wurmcraft;

import com.wurmcraft.common.CommonProxy;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.blocks.WurmTweaksBlocks;
import com.wurmcraft.common.command.WTCommand;
import com.wurmcraft.common.items.WurmTweaksItems;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.FunctionsRegistry;
import com.wurmcraft.script.WurmScript;
import com.wurmcraft.script.support.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.List;

@Mod (modid = Global.MODID, name = Global.NAME, version = Global.VERSION, dependencies = Global.DEPENDENCIES)
public class WurmTweaks {

 @Mod.Instance(Global.MODID)
 public static WurmTweaks instance;

 @SidedProxy (serverSide = Global.PROXY_SERVER, clientSide = Global.PROXY_CLIENT)
 public static CommonProxy proxy;

 public static WurmScript script;

 @Mod.EventHandler
 public void onPreInit(FMLPreInitializationEvent e) {
  WurmTweaksItems.register();
  WurmTweaksBlocks.register();
  proxy.preInit();
  FunctionsRegistry.register(new Minecraft());
  FunctionsRegistry.register(new AbyssalCraft());
  FunctionsRegistry.register(new ActuallyAdditions());
  FunctionsRegistry.register(new AE2());
  FunctionsRegistry.register(new AstralSorcery());
  FunctionsRegistry.register(new Avaritia());
  FunctionsRegistry.register(new BetterWithMods ());
  FunctionsRegistry.register(new BloodMagic());
  FunctionsRegistry.register (new Botania ());
  FunctionsRegistry.register (new Calculator ());
  FunctionsRegistry.register(new CharcoalPit());
  FunctionsRegistry.register(new DraconicEvolution());
  FunctionsRegistry.register(new EnvironmentalTech ());
  FunctionsRegistry.register(new Events ());
  FunctionsRegistry.register(new ExtraUtils2());
  FunctionsRegistry.register(new GalacticCraft ());
  FunctionsRegistry.register(new ImmersiveEngineering());
  FunctionsRegistry.register(new IndustrialForegoing());
  // TODO "Recipes should be registered before PostInit. Try net.minecraftforge.event.RegistryEvent.Register<IRecipe>'
//  FunctionsRegistry.register (new Mekanism ());
  FunctionsRegistry.register(new NuclearCraft());
  FunctionsRegistry.register (new OreStages ());
  FunctionsRegistry.register (new PneumaticCraft ());
  FunctionsRegistry.register(new SonarCore());
  FunctionsRegistry.register(new TConstruct());
  FunctionsRegistry.register(new TechReborn());
  FunctionsRegistry.register (new Thaumcraft ());
  FunctionsRegistry.register(new ThermalExpansion());
  FunctionsRegistry.register(new ToughAsNails());
  script = new WurmScript();
  if (ConfigHandler.checkForUpdates) WurmScript.downloadScripts();
 }

 @Mod.EventHandler
 public void onInit(FMLInitializationEvent e) {
  proxy.init();
  FunctionsRegistry.init();
 }

 public static final Runnable SCRIPT_MANAGER = () -> {
  Thread.currentThread().setName("Script Manager Thread");
  try {
   final List<Thread> scriptWorkers = new ArrayList<> ();
   final Bindings bindings = FunctionsRegistry.createBindings();
   WurmScript.getRunnableScripts().forEach(file -> {
    Thread script = new Thread(WurmScript.scriptToRunnable(file, bindings));
    script.setName(file.getName());
    scriptWorkers.add(script);
    script.start();
   });
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
    }
   } while (!allFinished);
  } catch (Exception e1) {
   System.err.println("EXCEPTION ENCOUNTERED IN SCRIPT MANAGER THREAD!");
   e1.printStackTrace();
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
  e.registerServerCommand(new WTCommand ());
 }

 @Mod.EventHandler
 public void onServerStopping(FMLServerStoppingEvent e) {

 }
}
