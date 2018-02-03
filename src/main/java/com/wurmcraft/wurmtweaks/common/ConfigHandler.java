package com.wurmcraft.wurmtweaks.common;

import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.reference.Local;
import com.wurmcraft.wurmtweaks.utils.InvalidRecipe;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber (modid = Global.MODID)
@Config (modid = Global.MODID)
public class ConfigHandler {

	@Config.LangKey (Local.CONFIG_DEBUG)
	public static boolean debug = false;

	@Config.LangKey (Local.CONFIG_REMOVE_ALL_CRAFTING_RECIPES)
	public static boolean removeAllCraftingRecipes = false;

	@Config.LangKey (Local.CONFIG_REMOVE_ALL_FURNACE_RECIPES)
	public static boolean removeAllFurnaceRecipes = false;

	@Config.LangKey (Local.CONFIG_COPYITEMNAME)
	public static boolean copyItemName = true;

	@Config.LangKey (Local.CONFIG_MASTER_SCRIPT)
	public static String masterScript = "https://raw.githubusercontent.com/Wurmcraft/WurmTweaks2/master/scripts/master.ws";

	@Config.LangKey (Local.CONFIG_RECIPE_UPDATES)
	public static boolean checkForRecipeUpdates = true;

	@Config.LangKey (Local.CONFIG_META_ITEM)
	public static String metaItems = "battery, cardboard, flippers, goggles, jetpackParts, largeReactor, reactor, mindControl, mechanicalComponent, nanoTech, solarPanel, thruster, flintChunk, flux, rawSteelIngot, machineFramework, quarryCore, mortar, magicChunk, aerogel, hyperDiamond, metallicHydrogen, quantumFoam, quantumSingularity, mixedSheet, boneChunk, creativeParts, creativePartsEnergy, creativePartsMagic, advMachineFramework, computationalCore, engineCoil, natureCore, token, advCraftingParts, gearMixedSheet, darkMatter, redMatter, unstableMatter";

	@Config.LangKey (Local.CONFIG_FILE_LOGGER)
	public static boolean logToFile = true;

	@Config.LangKey (Local.CONFIG_REMOVE_ALL_MACHINE_RECIPES)
	public static boolean removeAllMachineRecipes = false;

	@SubscribeEvent
	public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID ().equals (Global.MODID)) {
			ConfigManager.load (Global.MODID,Config.Type.INSTANCE);
			LogHandler.info ("Config Saved!");
		}
	}

	public static void handleLateConfigSettings () {
		if (removeAllCraftingRecipes)
			for (IRecipe recipe : ForgeRegistries.RECIPES.getValues ()) {
				ForgeRegistries.RECIPES.register (new InvalidRecipe (recipe));
			}
		if (removeAllFurnaceRecipes)
			FurnaceRecipes.instance ().getSmeltingList ().clear ();
	}
}
