package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Avaritia extends ModSupport {

	@Override
	public String getModID () {
		return "avaritia";
	}

	@Override
	public void init () {
		AvaritiaRecipeManager.EXTREME_RECIPES.clear ();
		AvaritiaRecipeManager.COMPRESSOR_RECIPES.clear ();
	}

	@ScriptFunction
	public void addShapedExtreme (String line) {
		String[] input = verify (line,line.split (" ").length >= 3,"addShapedExtreme('<output> <style> <format')");
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
				WurmScript.info ("Invalid Stack For '" + ch + "' " + invalidFormat.getOrDefault (ch,""));
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
			ExtremeShapedRecipe recipe = new ExtremeShapedRecipe (output,CraftingHelper.parseShaped (finalRecipe.toArray (new Object[0])));
			AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + recipe.hashCode ()),recipe);
		}
	}

	@ScriptFunction
	public void addShapelessExtreme (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addShapelessExtreme('<output> <input>...')");
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		NonNullList <Ingredient> inputStacks = NonNullList.create ();
		for (int index = 1; index < input.length; index++) {
			Ingredient tempInput = convertI (input[index]);
			if (tempInput != Ingredient.EMPTY)
				inputStacks.add (tempInput);
			else
				WurmScript.info ("Invalid Input '" + input[index] + "'");
		}
		if (inputStacks.size () > 0)
			AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + inputStacks.hashCode ()),new ExtremeShapelessRecipe (inputStacks,output));
		else
			WurmScript.info ("Invalid Recipe, No Items Found!");
	}

	@ScriptFunction
	public void addCompression (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addCompression('<output> <input>')");
		isValid (input[0],input[1]);
		ItemStack output = convertS (input[0]);
		AvaritiaRecipeManager.COMPRESSOR_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + convertS (input[1]).hashCode ()),new CompressorRecipe (output,convertS (input[1]).getCount (),false,NonNullList.from (convertI (input[1]))));
	}
}
