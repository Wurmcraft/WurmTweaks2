package com.wurmcraft.script.utils.recipe;

import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.WurmScript;
import com.wurmcraft.script.exception.InvalidStackException;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.StackSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 A Class of Helpfull Methods related to Recipes
 */
public class RecipeUtils {

	/**
	 Checks and Converts Script Object Strings into a List of useable Ingredient's

	 @param helper Instance of StackHelper
	 @param possibleItems List of the Items within the shapeless recipe

	 @return List of all valid recipe inputs
	 */
	public static List <Ingredient> getShapelessItems (StackHelper helper,String[] possibleItems) {
		List <Ingredient> shapelessItems = new ArrayList <> ();
		for (String item : possibleItems)
			if (!item.isEmpty ()) {
				Ingredient convertedIngredient = helper.convertIngredient (item);
				if (convertedIngredient != null && convertedIngredient != Ingredient.EMPTY && Objects.requireNonNull (convertedIngredient).getMatchingStacks ().length > 0)
					shapelessItems.add (convertedIngredient);
				else
					throw new InvalidStackException ("Invalid Ingredient '" + item + "'");
			}
		return shapelessItems;
	}

	public static ResourceLocation generateRecipeName (ItemStack output,Object... input) {
		return new ResourceLocation ("wurmtweaks",output.toString () + (output.hasTagCompound () ? "^" + output.getTagCompound () : "") + Arrays.hashCode (input));
	}

	private static int findLargest (int[] num) {
		int highest = 0;
		for (int i : num)
			if (i > highest)
				highest = i;
		return highest;
	}

	private static String replaceLastTillDiff (String line,char ch) {
		StringBuilder build = new StringBuilder (line);
		for (int index = line.length () - 1; index == 0; index--)
			if (line.charAt (index) == ch)
				build.deleteCharAt (index);
			else
				break;
		return build.toString ();
	}

	private static int[] getRecipeSize (String[] possibleStyle) {
		int[] temp = new int[possibleStyle.length];
		for (int index = 0; index < possibleStyle.length; index++)
			if (possibleStyle[index] != null && possibleStyle[index].length () > 0)
				temp[index] = replaceLastTillDiff (possibleStyle[index],'_').length ();
		int[] size = new int[2];
		size[0] = findLargest (temp);
		int height = 0;
		for (int t : temp)
			if (t > 0)
				height++;
		size[1] = height;
		return size;
	}

	public static List <Object> getShapedRecipe (StackHelper helper,String[] input) {
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++)
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
		int[] recipeSize = RecipeUtils.getRecipeSize (Arrays.copyOfRange (input,1,indexFirstVar));
		String[] recipeStyle = new String[recipeSize[1]];
		for (int index = 1; index < (recipeSize[1] + 1); index++) {
			StringBuilder temp = new StringBuilder (RecipeUtils.replaceLastTillDiff (input[index],'_'));
			if (temp.length () < recipeSize[0])
				while (temp.length () < recipeSize[0])
					temp.append (" ");
			recipeStyle[index - 1] = temp.toString ().replaceAll ("_"," ");
		}
		HashMap <Character, Ingredient> recipeFormat = new HashMap <> ();
		HashMap <Character, String> invalidFormat = new HashMap <> ();
		for (int index = (recipeSize[1] + 1); index < input.length; index++)
			if (!input[index].startsWith ("<") && input[index].length () == 1) {
				if ((index + 1) < input.length) {
					Ingredient stack = helper.convertIngredient (input[index + 1]);
					recipeFormat.put (input[index].charAt (0),stack);
					if (stack == Ingredient.EMPTY)
						invalidFormat.put (input[index].charAt (0),input[index + 1]);
					index++;
				} else
					recipeFormat.put (input[index].charAt (0),Ingredient.EMPTY);
			}
		boolean valid = true;
		for (Character ch : recipeFormat.keySet ())
			if (recipeFormat.get (ch) == Ingredient.EMPTY) {
				System.out.println ("Invalid Stack '" + ch + "' " + invalidFormat.getOrDefault (ch,""));
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
			return finalRecipe;
		}
		return null;
	}
}
