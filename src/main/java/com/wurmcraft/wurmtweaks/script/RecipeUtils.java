package com.wurmcraft.wurmtweaks.script;


import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.utils.DynamicShapedOreRecipe;
import com.wurmcraft.wurmtweaks.utils.DynamicShapelessOreRecipe;
import com.wurmcraft.wurmtweaks.utils.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class RecipeUtils {

	private static final ResourceLocation RECIPE_GROUP = new ResourceLocation (Global.NAME,"Recipes");
	private static final Random RAND = new Random (System.nanoTime ());
	public static List <IRecipe> activeRecipes = new ArrayList <> ();
	public static HashMap <ItemStack, ItemStack> activeFurnace = new HashMap <> ();

	public static void addShapeless (ItemStack output,Ingredient... inputItems) {
		for (Ingredient input : inputItems)
			if (input == Ingredient.EMPTY)
				return;
		DynamicShapelessOreRecipe recipe = new DynamicShapelessOreRecipe (RECIPE_GROUP,output,inputItems);
		recipe.setRegistryName (new ResourceLocation (Global.MODID,output.getUnlocalizedName () + Arrays.hashCode (inputItems) + RAND.nextInt (100)));
		ForgeRegistries.RECIPES.register (recipe);
		activeRecipes.add (recipe);
	}

	public static void addShaped (ItemStack output,Object... recipe) {
		if (!isValid (recipe))
			return;
		DynamicShapedOreRecipe shapedRecipe = new DynamicShapedOreRecipe (RECIPE_GROUP,output,recipe);
		shapedRecipe.setRegistryName (new ResourceLocation (Global.MODID,output.getUnlocalizedName () + output.getCount () + (output.hasTagCompound () ? output.getTagCompound ().hashCode () : "") + Arrays.hashCode (recipe) + RAND.nextInt (100)));
		ForgeRegistries.RECIPES.register (shapedRecipe);
		activeRecipes.add (shapedRecipe);
	}

	private static boolean isValid (Object... recipeInput) {
		for (Object obj : recipeInput)
			if (obj instanceof ItemStack) {
				if (obj == ItemStack.EMPTY)
					return false;
			} else if (obj instanceof Ingredient) {
				if (obj == Ingredient.EMPTY)
					return false;
			} else if (obj instanceof String)
				if (((String) obj).length () > 3 && !OreDictionary.doesOreNameExist (((String) obj)))
					return false;
		return true;
	}

	public static int countRecipeStyle (String style) {
		return (int) style.chars ().distinct ().count ();
	}

	public static void addFurnace (ItemStack output,ItemStack input,float exp) {
		FurnaceRecipes.instance ().addSmeltingRecipe (input,output,exp);
		activeFurnace.put (input,output);
	}

	public static void addBrewing (ItemStack output,ItemStack input,ItemStack bottom) {
		BrewingRecipeRegistry.addRecipe (new BrewingOreRecipe (input,Collections.singletonList (bottom),output));
	}

	public static int findLargest (int[] num) {
		int highest = 0;
		for (int i : num)
			if (i > highest)
				highest = i;
		return highest;
	}

	public static String replaceLastTillDiff (String line,char ch) {
		StringBuilder build = new StringBuilder (line);
		for (int index = line.length () - 1; index == 0; index--)
			if (line.charAt (index) == ch)
				build.deleteCharAt (index);
			else
				break;
		return build.toString ();
	}

	public static int[] getRecipeSize (String[] possibleStyle) {
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
		return size;
	}

	public static List <Ingredient> getShapelessRecipeItems (String[] input,Void empty,int startIndex) {
		List <Ingredient> inputStacks = new ArrayList <> ();
		for (int index = startIndex; index < input.length; index++)
			if (StackHelper.convert (input[index],null) != Ingredient.EMPTY)
				inputStacks.add (StackHelper.convert (input[index],null));
		return inputStacks;
	}

	public static List <Object> getRecipeItems (String[] input,int startIndex) {
		List <Object> inputStacks = new ArrayList <> ();
		for (int index = startIndex; index < input.length; index++)
			if (StackHelper.convert (input[index]) != ItemStack.EMPTY)
				inputStacks.add (StackHelper.convert (input[index]));
			else
				inputStacks.add (input[index]);
		return inputStacks;
	}

	public static List <Object> getShapedRecipe (String[] input) {
		int indexFirstVar = 1;
		for (; indexFirstVar < input.length; indexFirstVar++)
			if (input[indexFirstVar - 1].length () == 1 && input[indexFirstVar].contains ("<")) {
				indexFirstVar -= 1;
				break;
			}
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
					Ingredient stack = StackHelper.convert (input[index + 1],null);
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
			return finalRecipe;
		}
		return null;
	}

	// Fix Shaped Recipe Support (Just Rough Stack Export ATM)
	private String convert (IRecipe recipe) {
		StringBuilder builder = new StringBuilder ();
		if (recipe instanceof ShapedRecipes) {
			builder.append ("addShaped('");
			builder.append (StackHelper.convert (recipe.getRecipeOutput (),0));
			for (Ingredient obj : recipe.getIngredients ())
				builder.append (StackHelper.convert (obj));
		} else if (recipe instanceof ShapelessRecipes) {
			builder.append ("addShapeless('");
			builder.append (StackHelper.convert (recipe.getRecipeOutput (),0) + " ");
			for (Ingredient ingredient : recipe.getIngredients ())
				builder.append (StackHelper.convert (ingredient));
		}
		return builder.toString ();
	}
}
