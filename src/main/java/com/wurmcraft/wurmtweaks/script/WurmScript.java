package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;

import javax.script.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class WurmScript {

	private static final ScriptEngine engine = new ScriptEngineManager ().getEngineByName ("nashorn");
	public static File wurmScriptLocation = new File (Loader.instance ().getConfigDir () + File.separator + Global.NAME.replaceAll (" ",""));

	public static Bindings scriptFunctions = new SimpleBindings ();
	public static File currentScript = null;
	public static int lineNo = 0;
	public static final String SPACER_CHAR = "_";

	public void init () {
		scriptFunctions.put ("addShapeless",new AddShapeless ());
		scriptFunctions.put ("addShaped",new AddShaped ());
		scriptFunctions.put ("addFurnace",new AddFurnace ());
		scriptFunctions.put ("addBrewing",new AddBrewing ());
	}

	public void process (String line) {
		try {
			engine.eval (line,scriptFunctions);
			lineNo++;
		} catch (ScriptException e) {
			LogHandler.script (currentScript != null ? currentScript.getName () : "Code.ws",lineNo,e.getMessage ());
		}
	}

	public void process (String[] lines) {
		for (String line : lines)
			process (line);
	}

	public class AddShapeless implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] itemStrings = s.split (" ");
			ItemStack output = StackHelper.convert (itemStrings[0],null);
			if (output != ItemStack.EMPTY) {
				NonNullList <Ingredient> recipeInput = NonNullList.create ();
				for (int index = 1; index < itemStrings.length; index++)
					if (StackHelper.convert (itemStrings[index]) != Ingredient.EMPTY)
						recipeInput.add (StackHelper.convert (itemStrings[index]));
					else
						return null;
				RecipeUtils.addShapeless (output,recipeInput.toArray (new Ingredient[0]));
			} else if (!StackHelper.convert (itemStrings[0]).isSimple ()) {

			} else
				LogHandler.script (currentScript.getName (),lineNo,"Invalid Item '" + itemStrings[0] + "' For Shapeless Recipe Input");
			return null;
		}
	}

	public class AddShaped implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] recipeStrings = s.split (" ");
			ItemStack output = StackHelper.convert (recipeStrings[0],null);
			if (output != ItemStack.EMPTY) {
				if (recipeStrings.length % 2 != 0) {
					LogHandler.script (currentScript.getName (),lineNo,"Invalid Format '" + Strings.join (recipeStrings," ") + "' try <output> <recipe style> <varA>... <ItemA>...");
					return null;
				}
				List <String> recipeStyle = new ArrayList <> ();
				int recipeFormatStart = 4;
				for (int index = 1; index < 4; index++)
					if (recipeStrings[index].length () <= 3 && recipeStrings[index].length () != 1)
						recipeStyle.add (recipeStrings[index].replaceAll (SPACER_CHAR," "));
					else if (recipeStrings[index].length () != 1) {
						recipeFormatStart = index + 1;
						break;
					}
				HashMap <Character, Ingredient> recipeFormat = new HashMap <> ();
				for (int index = recipeFormatStart; index < recipeStrings.length; index++) {
					if (recipeStrings[index].length () == 1) {
						Character formatChar = recipeStrings[index].charAt (0);
						index++;
						Ingredient formatIngredient = StackHelper.convert (recipeStrings[index]);
						if (!formatIngredient.equals (Ingredient.EMPTY))
							recipeFormat.put (formatChar,formatIngredient);
						else {
							LogHandler.script (getScriptName (),lineNo,recipeStrings[index] + " is not a valid Ingredient, try using /wt hand");
							return null;
						}
					} else {
						LogHandler.script (getScriptName (),lineNo,"Invalid Varable Format '" + recipeStrings[index] + "', try using 'Var'");
						return null;
					}
				}
				if (RecipeUtils.countRecipeStyle (Strings.join (recipeStyle.toArray (new String[0]),"")) != recipeFormat.keySet ().size ()) {
					LogHandler.script (getScriptName (),lineNo,"Inpossible Varable Style to Format, check to make sure you have used all the varables in the recipe style!");
					return null;
				}
				List <Object> temp = new ArrayList <> ();
				for (Character ch : recipeFormat.keySet ()) {
					temp.add (ch);
					temp.add (recipeFormat.get (ch));
				}
				List <Object> finalRecipe = new ArrayList <> ();
				finalRecipe.addAll (recipeStyle);
				finalRecipe.addAll (temp);
				RecipeUtils.addShaped (output,finalRecipe.toArray (new Object[0]));
			}
			return null;
		}
	}

	public class AddFurnace implements Function <String, Void> {

		@Override
		public Void apply (String s) {
			String[] recipeStrings = s.split (" ");
			ItemStack output = StackHelper.convert (recipeStrings[0],null);
			if (output != ItemStack.EMPTY && recipeStrings.length > 1) {
				ItemStack input = StackHelper.convert (recipeStrings[1],null);
				if (input != ItemStack.EMPTY) {
					try {
						float exp = Float.parseFloat (recipeStrings[2]);
						if (exp > 0)
							RecipeUtils.addFurnace (output,input,exp);
						else
							LogHandler.script (getScriptName (),lineNo,"Furnace Experience Must Be Greater Than 0 (>0)");
					} catch (NumberFormatException e) {
						LogHandler.script (getScriptName (),lineNo,recipeStrings[2] + " is not a valid number!");
					}
				} else
					LogHandler.script (getScriptName (),lineNo,"Invalid Input Stack '" + recipeStrings[1] + "'!");
			}
			return null;
		}
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
}
