package com.wurmcraft.common;

import com.wurmcraft.common.reference.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Manages and handles anything to do with global config's
 */
@Mod.EventBusSubscriber(modid = Global.MODID)
@Config(modid = Global.MODID)
public class ConfigHandler {

  @Config.Comment("Master Script URL")
  public static String masterScript =
      "https://raw.githubusercontent.com/Wurmcraft/WurmTweaks2/Rewrite/examples/master.ws";

  @Config.Comment("Default Script Directory")
  public static String scriptDir = "config/WurmTweaks";

  @Config.Comment("List of Meta Items")
  public static String metaItems =
      "battery, cardboard, flippers, goggles, jetpackParts, largeReactor, reactor, mindControl, mechanicalComponent, "
          + "nanoTech, solarPanel, thruster, flintChunk, flux, rawSteelIngot, machineFramework, quarryCore, mortar, "
          + "magicChunk, aerogel, hyperDiamond, metallicHydrogen, quantumFoam, quantumSingularity, mixedSheet, boneChunk, "
          + "creativeParts, creativePartsEnergy, creativePartsMagic, advMachineFramework, computationalCore, engineCoil, "
          + "natureCore, token, advCraftingParts, gearMixedSheet, darkMatter, redMatter, unstableMatter, shielding, "
          + "neuralS, neuralN, superComputer, antiMatter, rocketEngine, electroShielding, lithium";

  @Config.Comment("Check for Script Updates")
  public static boolean checkForUpdates = true;

  @Config.Comment("Recipes not removed when removing all recipes")
  public static String[] recipeWhitelist = new String[]{};

  @Config.Comment("Remove all recipes (Except for whitelist)")
  public static boolean removeAllRecipes = true;

  @Config.Comment("Remove all machine recipes")
  public static boolean removeAllMachineRecipes = true;

  @Config.Comment("MB per ingot with TConstruct")
  public static int tinkersConstructIngotAmount = 1000;

  @Config.Comment("Amount of Ingots per block")
  public static int tinkersConstructBlockMultiplier = 32;

  @Config.Comment("Minecraft window title name")
  public static String title = "";

  @Config.Comment("Remove all Loot")
  public static boolean removeAllLoot = true;

  @Config.Comment("Change how damage is calculated")
  public static double damageMod = 1.0;

  @Config.Comment("Change fast regen is calculated")
  public static double regenMod = 1.0;

  @SubscribeEvent
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Global.MODID)) {
      ConfigManager.load(Global.MODID, Config.Type.INSTANCE);
    }
  }
}
