package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.reference.Global;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeUtils {

	private static final ResourceLocation RECIPE_GROUP = new ResourceLocation (Global.NAME,"Recipes");
	public static List <IRecipe> activeRecipes = new ArrayList <> ();
	public static HashMap <ItemStack, ItemStack> activeFurnace = new HashMap <> ();

	public static void addShapeless (ItemStack output,Ingredient... inputItems) {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe (RECIPE_GROUP,output,inputItems);
		recipe.setRegistryName (new ResourceLocation (Global.MODID,output.getUnlocalizedName ()));
		ForgeRegistries.RECIPES.register (recipe);
		activeRecipes.add (recipe);
	}

	public static void addShaped (ItemStack output,Object... recipe) {
		ShapedOreRecipe shapedRecipe = new ShapedOreRecipe (RECIPE_GROUP,output,recipe);
		shapedRecipe.setRegistryName (new ResourceLocation (Global.MODID,output.getUnlocalizedName ()));
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

	public static void addBrewing (ItemStack output,ItemStack input,List <ItemStack> inputs) {
		BrewingRecipeRegistry.addRecipe (new BrewingOreRecipe (input,inputs,output));
	}
}
