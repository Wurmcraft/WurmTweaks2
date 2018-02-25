package com.wurmcraft.wurmtweaks.script.support;


import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Minecraft extends ModSupport {

	@Override
	public String getModID () {
		return "minecraft";
	}

	@Override
	public void init () {

	}

	@ScriptFunction
	public void addShapeless (String line) {
		String[] input = verify (line,(line.split (" ").length >= 2 && line.split (" ").length <= 10),"addShapeless('<output> <input>...')");
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = 1; index < input.length; index++) {
			isValid (input[index]);
			inputStacks.add (convertI (input[index]));
		}
		if (inputStacks.size () > 0)
			RecipeUtils.addShapeless (output,inputStacks.toArray (new Ingredient[0]));
		else
			WurmScript.info ("Invalid Recipe, No Items Found!");
	}

	@ScriptFunction
	public void addShaped (String line) {
		String[] input = verify (line,line.split (" ").length > 4,"addShaped(<output> <style> <format>')");
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++) {
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
		}
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		int[] recipeSize = RecipeUtils.getRecipeSize (Arrays.copyOfRange (input,1,indexFirstVar));
		String[] recipeStyle = new String[recipeSize[1]];
		for (int index = 1; index < (recipeSize[1] + 1); index++) {
			StringBuilder temp = new StringBuilder (RecipeUtils.replaceLastTillDiff (input[index],WurmScript.SPACER));
			if (temp.length () < recipeSize[0])
				while (temp.length () < recipeSize[0])
					temp.append (" ");
			recipeStyle[index - 1] = temp.toString ().replaceAll (WurmScript.SPACER + ""," ");
		}
		HashMap <Character, Ingredient> recipeFormat = new HashMap <> ();
		HashMap <Character, String> invalidFormat = new HashMap <> ();
		for (int index = (recipeSize[1] + 1); index < input.length; index++)
			if (!input[index].startsWith ("<") && input[index].length () == 1) {
				if ((index + 1) < input.length) {
					Ingredient stack = convertI (input[index + 1]);
					recipeFormat.put (input[index].charAt (0),stack);
					if (stack == Ingredient.EMPTY)
						invalidFormat.put (input[index].charAt (0),input[index + 1]);
					index++;
				} else
					recipeFormat.put (input[index].charAt (0),Ingredient.EMPTY);
			} else if (input[index].length () > 1) {
				WurmScript.info ("Invalid Format, '" + input[index] + " Should Be A Single Character!");
				return;
			}
		boolean valid = true;
		for (Character ch : recipeFormat.keySet ())
			if (recipeFormat.get (ch) == Ingredient.EMPTY) {
				WurmScript.info ("Invalid Stack '" + ch + "' " + invalidFormat.getOrDefault (ch,""));
				valid = false;
			}
		if (valid) {
			List <Object> temp = new ArrayList <> ();
			for (Character ch : recipeFormat.keySet ()) {
				temp.add (ch);
				temp.add (recipeFormat.get (ch));
			}
			List <Object> finalRecipe = new ArrayList <> ();
			finalRecipe.addAll (Arrays.asList (recipeStyle));
			finalRecipe.addAll (temp);
			RecipeUtils.addShaped (output,finalRecipe.toArray (new Object[0]));
		}
	}

	@ScriptFunction (linkSize = 3, link = "furnace")
	public void addFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addFurnace('<output> <input> <exp>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.FLOATNG,input[2]);
		RecipeUtils.addFurnace (convertS (input[0]),convertS (input[1]),convertNF (input[2]));
	}

	@ScriptFunction
	public void addOreEntry (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addOreEntry('<stack> entry')");
		isValid (input[0]);
		for (int index = 1; index < input.length; index++) {
			isValid (EnumInputType.STRING,input[index]);
			OreDictionary.registerOre (input[index].replaceAll ("[<>]",""),convertS (input[0]));
		}
	}

	@ScriptFunction
	public void addBrewing (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addBrewing('<output> <input> <bottom>')");
		for (int index = 0; index < 2; index++)
			isValid (input[index]);
		RecipeUtils.addBrewing (convertS (input[0]),convertS (input[1]),convertS (input[2]));
	}
}
