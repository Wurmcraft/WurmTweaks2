package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.api.WurmTweaks2API;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.utils.InvalidRecipe;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.script.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WurmScript extends WurmTweaks2API {

	public static final char SPACER = '_';
	private static final ScriptEngine engine = new ScriptEngineManager (null).getEngineByName ("nashorn");
	public static final File wurmScriptLocation = new File (Loader.instance ().getConfigDir () + File.separator + Global.NAME.replaceAll (" ",""));
	public static Bindings scriptFunctions = null;
	public File currentScript = null;
	public int lineNo = 0;
	public static boolean reload = false;
	protected LinkedRegistry linkRegistry = new LinkedRegistry ();

	public void setCurrentScript (File currentScript) {
		this.currentScript = currentScript;
		lineNo = 1;
	}

	public void info (String msg) {
		LogHandler.script (getScriptName (),lineNo,msg);
	}

	public static void info(String msg, Void empty) {
		LogHandler.script ("Thread",0,msg);
	}

	public String getScriptName () {
		return currentScript != null ? currentScript.getName () : "Code.ws";
	}

	public static String[] removeComments (String[] withComments) {
		List <String> without = new ArrayList <> ();
		for (int x = 0; x < withComments.length; x++) {
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
				if (Loader.isModLoaded (controller.getModID ()) || controller.getModID ().equals ("minecraft") || controller.getModID ().equals ("events")) {
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

	// TODO FIX
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
				if (ConfigHandler.linkedMachines) {
					String linkedMachine = line.substring (0,line.indexOf ("('")).replaceAll ("add","").toLowerCase ();
					Method[] methods = linkRegistry.getLinkedMachines (linkedMachine);
					String[] recipes = linkRegistry.getLinkedMachinesRecipes (linkedMachine,line);
					if (methods.length > 0 && recipes.length > 0)
						for (String ln : linkRegistry.getLinkedMachinesRecipes (linkedMachine,line))
							engine.eval (ln,scriptFunctions);
					else
						engine.eval (line,scriptFunctions);
				} else
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

	public void process (File file) {
		try {
			engine.eval (new FileReader (file),scriptFunctions);
		} catch (FileNotFoundException | ScriptException e) {
			e.printStackTrace ();
		}
	}
}