package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.event.ScriptEvents;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.utils.InvalidRecipe;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class WurmScript {

	private static final ScriptEngine engine = new ScriptEngineManager (null).getEngineByName ("nashorn");
	public static File wurmScriptLocation = new File (Loader.instance ().getConfigDir () + File.separator + Global.NAME.replaceAll (" ",""));

	public static Bindings scriptFunctions = null;
	public static File currentScript = null;
	public static int lineNo = 0;
	public static final char SPACER = '_';
	public static final String SPACER_CHAR = "_";
	public static final List <IModSupport> activeControllers = new ArrayList <> ();
	public static boolean reload = false;

	public void init () {
		if (scriptFunctions == null)
			scriptFunctions = new SimpleBindings ();
		scriptFunctions.put ("addBrewing",new AddBrewing ());
		scriptFunctions.put ("addOreEntry",new AddOreEntry ());
		scriptFunctions.put ("disablePickup",new DisablePickup ());
		scriptFunctions.put ("convertPickup",new ConvertPickup ());
		scriptFunctions.put ("addTooltip",new AddToolTip ());
		scriptFunctions.put ("isModLoaded",new IsModLoaded ());
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

	public static void setCurrentScript (File currentScript) {
		WurmScript.currentScript = currentScript;
		lineNo = 1;
	}

	public static void register (IModSupport controller) {
		if (!activeControllers.contains (controller))
			activeControllers.add (controller);
		else
			LogHandler.info (controller.getModID () + " has already been registered!");
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

	public class AddBrewing implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] itemStrings = s.split (" ");
			ItemStack output = StackHelper.convert (itemStrings[0],null);
			if (output != ItemStack.EMPTY) {
				ItemStack input = StackHelper.convert (itemStrings[1],null);
				if (input != null) {
					List <ItemStack> recipeInput = new ArrayList <> ();
					for (int index = 2; index < itemStrings.length; index++)
						if (StackHelper.convert (itemStrings[index],null) != ItemStack.EMPTY)
							recipeInput.add (StackHelper.convert (itemStrings[index],null));
						else
							return null;
					RecipeUtils.addBrewing (output,input,recipeInput);
				} else
					LogHandler.script (getScriptName (),lineNo,"Invalid Input Item '" + itemStrings[1] + "'");
			} else
				LogHandler.script (getScriptName (),lineNo,"Invalid Item '" + itemStrings[0] + "' For Shapeless Recipe Input");
			return null;
		}
	}

	public class AddOreEntry implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] itemStrings = s.split (" ");
			ItemStack item = StackHelper.convert (itemStrings[0],null);
			if (item != ItemStack.EMPTY) {
				if (itemStrings.length > 1) {
					for (int index = 1; index < itemStrings.length; index++)
						if (itemStrings[index] != null && itemStrings[index].length () > 0)
							OreDictionary.registerOre (itemStrings[index],item);
				} else
					LogHandler.script (getScriptName (),lineNo,"Missing Ore Dict Entry [addOreEntry(<stack> <entry1>...)]");
			}
			return null;
		}
	}

	public class DisablePickup implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] stack = s.split (" ");
			ItemStack item = StackHelper.convert (stack[0],null);
			if (item != ItemStack.EMPTY) {
				ScriptEvents.addThrowCancelEvent (item);
			} else
				LogHandler.script (getScriptName (),lineNo,stack[0] + " is not a valid item");
			return null;
		}
	}

	public class ConvertPickup implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] itemString = s.split (" ");
			if (itemString.length == 2) {
				ItemStack pickupItem = StackHelper.convert (itemString[0],null);
				ItemStack convert = StackHelper.convert (itemString[1],null);
				if (pickupItem != ItemStack.EMPTY && convert != ItemStack.EMPTY) {
					ScriptEvents.addPickupConversion (pickupItem,convert);
				} else
					LogHandler.script (getScriptName (),lineNo,s + " does not a have a valid item");
			} else
				LogHandler.script (getScriptName (),lineNo,"convertPickup('<itemA> <itemB>')");
			return null;
		}
	}

	public class AddToolTip implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] itemStrings = s.split (" ");
			if (itemStrings.length >= 2) {
				ItemStack item = StackHelper.convert (itemStrings[0],null);
				List <String> tooltip = new ArrayList <> ();
				for (int index = 1; index < itemStrings.length; index++)
					tooltip.add (itemStrings[index].replaceAll ("&","§").replaceAll ("_"," "));
				ScriptEvents.addToolTipEntry (item,tooltip.toArray (new String[0]));
			} else
				LogHandler.script (getScriptName (),lineNo,"addTooltip('<item> <tipA> <tipB>...");
			return null;
		}
	}

	public class IsModLoaded implements Function <String, Boolean> {

		@Override
		public Boolean apply (String s) {
			String[] mod = s.split (" ");
			if (mod.length == 1) {
				return Loader.isModLoaded (mod[0]);
			} else
				WurmScript.info ("isModLoaded('<ModName>')");
			return false;
		}
	}
}
