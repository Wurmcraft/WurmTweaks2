package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.IModSupport;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.WurmScript;
import com.wurmcraft.wurmtweaks.utils.LogHandler;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
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

public class Avaritia implements IModSupport {

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
		String[] input = line.split (" ");
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++) {
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
		}
		if (input.length > 4) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				int[] recipeSize = getRecipeSize (Arrays.copyOfRange (input,1,indexFirstVar));
				String[] recipeStyle = new String[recipeSize[1]];
				for (int index = 1; index < (recipeSize[1] + 1); index++) {
					StringBuilder temp = new StringBuilder (replaceLastTillDiff (input[index],WurmScript.SPACER));
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
							Ingredient stack = StackHelper.convert (input[index + 1]);
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
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addShapedExtreme(<output> <style> <format>')");
	}

	@ScriptFunction
	public void addShapelessExtreme (String line) {
		String[] input = line.split (" ");
		if (input.length > 1) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				NonNullList <Ingredient> inputStacks = NonNullList.create ();
				for (int index = 1; index < input.length; index++) {
					Ingredient tempInput = StackHelper.convert (input[index]);
					if (tempInput != Ingredient.EMPTY)
						inputStacks.add (tempInput);
					else
						WurmScript.info ("Invalid Input '" + input[index] + "'");
				}
				if (inputStacks.size () > 0)
					AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + inputStacks.hashCode ()),new ExtremeShapelessRecipe (inputStacks,output));
				else
					WurmScript.info ("Invalid Recipe, No Items Found!");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addExtremeShapeless('<output> <input(s)>...')");
	}

	@ScriptFunction
	public void addCompression (String line) {
		String[] input = line.split (" ");
		if (input.length == 2) {
			ItemStack output = StackHelper.convert (input[0],null);
			if (output != ItemStack.EMPTY) {
				Ingredient inputStack = StackHelper.convert (input[1]);
				if (inputStack != Ingredient.EMPTY) {
					List <Ingredient> inputFormatted = new ArrayList <> ();
					inputFormatted.add (inputStack);
					CompressorRecipe recipe = new CompressorRecipe (output,StackHelper.convert (input[1],null).getCount (),false,inputFormatted);
					AvaritiaRecipeManager.COMPRESSOR_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + inputStack.hashCode ()),recipe);
				} else
					WurmScript.info ("Invalid Input '" + input[1] + "'");
			} else
				WurmScript.info ("Invalid Output '" + input[0] + "'");
		} else
			WurmScript.info ("addCompression('<output> <input>')");
	}

	public int[] getRecipeSize (String[] possibleStyle) {
		int[] temp = new int[possibleStyle.length];
		for (int index = 0; index < possibleStyle.length; index++)
			if (possibleStyle[index] != null && possibleStyle[index].length () > 0)
				temp[index] = replaceLastTillDiff (possibleStyle[index],WurmScript.SPACER).length ();
		int[] size = new int[2];
		size[0] = findLargest (temp);
		int height = 0;
		for (int t : temp)
			if (t > 0)
				height++;
		size[1] = height;
		LogHandler.info ("Size: " + size[0] + " " + size[1]);
		return size;
	}

	private String replaceLastTillDiff (String line,char ch) {
		StringBuilder build = new StringBuilder (line);
		for (int index = line.length () - 1; index == 0; index--)
			if (line.charAt (index) == ch)
				build.deleteCharAt (index);
			else
				break;
		return build.toString ();
	}

	private int findLargest (int[] num) {
		int highest = 0;
		for (int i : num)
			if (i > highest)
				highest = i;
		return highest;
	}
}
