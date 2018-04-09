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
	public static String metaItems = "battery, cardboard, flippers, goggles, mechanicalComponent, nanoTech, solarPanel, aerogel, hyperDiamond, engineCoil";

	@Config.LangKey (Local.CONFIG_FILE_LOGGER)
	public static boolean logToFile = true;

	@Config.LangKey (Local.CONFIG_REMOVE_ALL_MACHINE_RECIPES)
	public static boolean removeAllMachineRecipes = false;

	@Config.LangKey (Local.CONFIG_LINKED_MACHINES)
	public static boolean linkedMachines = true;

	@Config.LangKey (Local.CONFIG_CACHE)
	public static boolean cache = true;

	@Config.LangKey (Local.CONFIG_EMPTY_STACK)
	public static String emptyStack = "empty";

	@Config.LangKey (Local.CONFIG_SPACE_CHAR)
	public static String spaceChar = "%";

	@Config.LangKey (Local.CONFIG_NBT_CHAR)
	public static String nbtChar = "^";

	@Config.LangKey (Local.CONFIG_META_CHAR)
	public static String metaChar = "@";

	@Config.LangKey (Local.CONFIG_START_CHAR)
	public static String startChar = "<";

	@Config.LangKey (Local.CONFIG_END_CHAR)
	public static String endChar = ">";

	@Config.LangKey (Local.CONFIG_SIZE_CHAR)
	public static String sizeChar = "x";

	@Config.LangKey (Local.CONFIG_FLUID_CHAR)
	public static String fluidChar = "*";

	@Config.LangKey (Local.CONFIG_GAS_CHAR)
	public static String gasChar = "%";

	@Config.LangKey (Local.CONFIG_DAMAGE_MOD)
	public static double damageMod = 1.0;

	@SubscribeEvent
	public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID ().equals (Global.MODID)) {
			ConfigManager.load (Global.MODID,Config.Type.INSTANCE);
			LogHandler.info ("Config Saved!");
		}
	}

	public static void handleLateConfigSettings () {
		if (removeAllCraftingRecipes)
			for (IRecipe recipe : ForgeRegistries.RECIPES.getValues ())
				ForgeRegistries.RECIPES.register (new InvalidRecipe (recipe));
		if (removeAllFurnaceRecipes)
			FurnaceRecipes.instance ().getSmeltingList ().clear ();
	}
}
