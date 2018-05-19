package com.wurmcraft.common;

import com.wurmcraft.common.reference.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Manages and handles anything to do with global config's
 */
@Mod.EventBusSubscriber(modid = Global.MODID)
@Config(modid = Global.MODID)
public class ConfigHandler {

 @Config.Comment("Master Script URL")
 public static String masterScript =
  "https://raw.githubusercontent.com/Wurmcraft/WurmTweaks2/master/scripts/master.ws";

 @Config.Comment("Default Script Directory")
 public static String scriptDir = "config" + File.separator + "WurmTweaks";

 @Config.Comment("Enable Debug Mode")
 public static boolean debug = true;

 @Config.Comment("Debug Log Directory")
 public static String logDirectory = "wurmscript_log";

 @Config.Comment("Interval, in milliseconds, to check for script updates")
 public static long updateInterval = 1800000l;

 @Config.Comment("List of Meta Items")
 public static String metaItems =
  "battery, cardboard, flippers, goggles, jetpackParts, largeReactor, reactor, mindControl, mechanicalComponent, " +
  "nanoTech, solarPanel, thruster, flintChunk, flux, rawSteelIngot, machineFramework, quarryCore, mortar, " +
  "magicChunk, aerogel, hyperDiamond, metallicHydrogen, quantumFoam, quantumSingularity, mixedSheet, boneChunk, " +
  "creativeParts, creativePartsEnergy, creativePartsMagic, advMachineFramework, computationalCore, engineCoil, " +
  "natureCore, token, advCraftingParts, gearMixedSheet, darkMatter, redMatter, unstableMatter, shielding, " +
  "neuralS, neuralN, superComputer, antiMatter, rocketEngine, electroShielding, lithium";

 @Config.Comment("Should delete old scripts")
 public static boolean deleteOld = false;

 @Config.Comment("Remove All Crafting Recipes")
 public static boolean removeAllCraftingRecipes = true;

 @Config.Comment("Remove All Furnace Recipes")
 public static boolean removeAllFurnaceRecipes = true;

 @Config.Comment("Remove All Machine Recipes")
 public static boolean removeAllMachineRecipes = true;

 @Config.Comment("Will not remove recipe from this mod (modid)")
 public static String[] recipeWhitelist = new String[]{"harvestcraft"};

 @Config.Comment("Check for Script Updates")
 public static boolean checkForUpdates = false;

 @Config.Comment("Default TConstruct Ingot Fluid Amount")
 public static int tinkersConstructIngotAmount = 144;

 @Config.Comment("Default TConstruct Block Fluid Multiplier (ingot * x) = total fluid for a block")
 public static int tinkersConstructBlockMultiplier = 9;

 @SubscribeEvent
 public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
  if (event.getModID().equals(Global.MODID))
   ConfigManager.load(Global.MODID, Config.Type.INSTANCE);
 }
}
