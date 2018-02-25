package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.utils.InvalidRecipe;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WurmScript extends WurmTweaks2API {

	public static final char SPACER = '_';
	private static final ScriptEngine engine = new ScriptEngineManager (null).getEngineByName ("nashorn");
	public static File wurmScriptLocation = new File (Loader.instance ().getConfigDir () + File.separator + Global.NAME.replaceAll (" ",""));
	public static Bindings scriptFunctions = null;
	public static File currentScript = null;
	public static int lineNo = 0;
	public static boolean reload = false;

	public static void setCurrentScript (File currentScript) {
		WurmScript.currentScript = currentScript;
		lineNo = 1;
	}

	public static void info (String msg) {
		LogHandler.script (getScriptName (),lineNo,msg);
	}

	public static String getScriptName () {
		return currentScript != null ? currentScript.getName () : "Code.ws";
	}

	public static String[] removeComments (String[] withComments) {
		List <String> without = new ArrayList <> ();
		for (int x = 0; x < withComments.length; x++) {
			if (withComments[x].startsWith ("//") || withComments[x].replaceAll (" ","").startsWith ("//"))
				continue;
			if (withComments[x].startsWith ("/*") || withComments[x].replaceAll (" ","").startsWith ("/*"))
				for (int y = (x + 1); y < withComments.length; y++)
					if (withComments[y].startsWith ("*/") || withComments[y].replaceAll (" ","").startsWith ("*/")) {
						x = y + 1;
						break;
					}
			without.add (withComments[x]);
		}
		return without.toArray (new String[0]);
	}

	public static List <IModSupport> getActiveControllers () {
		return activeControllers;
	}

	public void init () {
		if (scriptFunctions == null)
			scriptFunctions = new SimpleBindings ();
		if (activeControllers.size () > 0) {
			for (IModSupport controller : activeControllers)
				if (Loader.isModLoaded (controller.getModID ()) || controller.getModID ().equals ("minecraft")) {
					LogHandler.info ("Loaded " + controller.getModID () + " ModSupport");
					controller.init ();
					Method[] methods = controller.getClass ().getDeclaredMethods ();
					for (Method method : methods)
						if (method.getAnnotation (ScriptFunction.class) != null) {
							scriptFunctions.put (method.getName ().toLowerCase (),new ScriptFunctionWrapper (controller,method));
							scriptFunctions.put (method.getName (),new ScriptFunctionWrapper (controller,method));
						}
				}
		} else
			info ("Warning: No Controllers Exist!");
	}

	public void reload () {
		reload = true;
		scriptFunctions = null;
		ForgeRegistry <IRecipe> recipeRegistry = (ForgeRegistry <IRecipe>) ForgeRegistries.RECIPES;
		recipeRegistry.unfreeze ();
		for (IRecipe recipe : RecipeUtils.activeRecipes) {
			recipeRegistry.remove (recipe.getRegistryName ());
			recipeRegistry.register (new InvalidRecipe (recipe));
		}
		RecipeUtils.activeRecipes.clear ();
		for (ItemStack input : RecipeUtils.activeFurnace.keySet ())
			FurnaceRecipes.instance ().getSmeltingList ().remove (input,RecipeUtils.activeFurnace.get (input));
		RecipeUtils.activeFurnace.clear ();
	}

	public void process (String line) {
		if (!line.startsWith ("//") && line.length () > 0)
			try {
				engine.eval (line,scriptFunctions);
			} catch (Exception e) {
				info (e.getLocalizedMessage ());
			}
	}

	public void process (String[] lines) {
		for (String line : lines) {
			process (line);
			lineNo++;
		}
	}
}