package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.utils.DynamicShapedOreRecipe;
import com.wurmcraft.wurmtweaks.utils.DynamicShapelessOreRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeUtils {

	private static final ResourceLocation RECIPE_GROUP = new ResourceLocation (Global.NAME,"Recipes");
	public static List <IRecipe> activeRecipes = new ArrayList <> ();
	public static HashMap <ItemStack, ItemStack> activeFurnace = new HashMap <> ();

	public static void addShapeless (ItemStack output,Ingredient... inputItems) {
		DynamicShapelessOreRecipe recipe = new DynamicShapelessOreRecipe (RECIPE_GROUP,output,inputItems);
		recipe.setRegistryName (new ResourceLocation (Global.MODID,output.getUnlocalizedName () + Arrays.hashCode (inputItems)));
		ForgeRegistries.RECIPES.register (recipe);
		activeRecipes.add (recipe);
	}

	public static void addShaped (ItemStack output,Object... recipe) {
		DynamicShapedOreRecipe shapedRecipe = new DynamicShapedOreRecipe (RECIPE_GROUP,output,recipe);
		shapedRecipe.setRegistryName (new ResourceLocation (Global.MODID,output.getUnlocalizedName () + Arrays.hashCode (recipe)));
		ForgeRegistries.RECIPES.register (shapedRecipe);
		activeRecipes.add (shapedRecipe);
	}

	public static int countRecipeStyle (String style) {
		return (int) style.chars ().distinct ().count ();
	}

	public static void addFurnace (ItemStack output,ItemStack input,float exp) {
		FurnaceRecipes.instance ().addSmeltingRecipe (input,output,exp);
		activeFurnace.put (input,output);
	}

	public static void addBrewing (ItemStack output,ItemStack input,ItemStack bottom) {
		BrewingRecipeRegistry.addRecipe (new BrewingOreRecipe (input,Arrays.asList (bottom),output));
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
}
