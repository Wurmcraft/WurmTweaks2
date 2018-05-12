package com.wurmcraft.common;

import com.wurmcraft.common.reference.Global;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 Manages and handles anything to do with global config's
 */
public class ConfigHandler {

	@Config (modid = "wurmtweaks")
	public static class General {

		@Config.Comment ("Master Script URL")
		public static String masterScript = "https://raw.githubusercontent.com/Wurmcraft/WurmTweaks2/Rework/scripts/master.ws";

		@Config.Comment ("Enable Debug Mode")
		public static boolean debug = false;

		@Config.Comment ("List of Meta Items")
		public static String metaItems = "battery, cardboard, flippers, goggles, jetpackParts, largeReactor, reactor, mindControl, mechanicalComponent, nanoTech, solarPanel, thruster, flintChunk, flux, rawSteelIngot, machineFramework, quarryCore, mortar, magicChunk, aerogel, hyperDiamond, metallicHydrogen, quantumFoam, quantumSingularity, mixedSheet, boneChunk, creativeParts, creativePartsEnergy, creativePartsMagic, advMachineFramework, computationalCore, engineCoil, natureCore, token, advCraftingParts, gearMixedSheet, darkMatter, redMatter, unstableMatter, shielding, neuralS, neuralN, superComputer, antiMatter, rocketEngine, electroShielding, lithium";

		@SubscribeEvent
		public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID ().equals (Global.MODID))
				ConfigManager.load (Global.MODID,Config.Type.INSTANCE);
		}
	}


	@Config (modid = "wurmtweaks", category = "script")
	public static class Script {

		@Config.Comment ("Remove All Crafting Recipes")
		public static boolean removeAllCraftingRecipes = true;

		@Config.Comment ("Remove All Furnace Recipes")
		public static boolean removeAllFurnaceRecipes = true;

		@Config.Comment ("Remove All Machine Recipes")
		public static boolean removeAllMachineRecipes = true;

		@Config.Comment ("Will not remove recipe from this mod (modid)")
		public static String[] recipeWhitelist = new String[] {"harvestcraft"};

		@Config.Comment ("Check for Script Updates")
		public static boolean checkForUpdates = true;

		@SubscribeEvent
		public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID ().equals (Global.MODID))
				ConfigManager.load (Global.MODID,Config.Type.INSTANCE);
		}
	}
}
