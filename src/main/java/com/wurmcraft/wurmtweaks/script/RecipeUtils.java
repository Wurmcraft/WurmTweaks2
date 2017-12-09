package com.wurmcraft.wurmtweaks.script;

import com.wurmcraft.wurmtweaks.reference.Global;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class RecipeUtils {

	private static final ResourceLocation RECIPE_GROUP = new ResourceLocation (Global.NAME,"Recipes");

	public static void addShapeless (ItemStack output,Ingredient... inputItems) {
		GameRegistry.addShapelessRecipe (new ResourceLocation (Global.MODID,output.getDisplayName ()),RECIPE_GROUP,output,inputItems);
	}

	public static void addShaped (ItemStack output,Object... recipe) {
		GameRegistry.addShapedRecipe (new ResourceLocation (Global.MODID,output.getDisplayName ()),RECIPE_GROUP,output,recipe);
	}

	public static int countRecipeStyle (String style) {
		return (int) style.chars ().distinct ().count ();
	}

	public static void addFurnace (ItemStack output,ItemStack input,float exp) {
		FurnaceRecipes.instance ().addSmeltingRecipe (input,output,exp);
	}

	public static void addBrewing (ItemStack output,ItemStack input,List <ItemStack> inputs) {
		BrewingRecipeRegistry.addRecipe (new BrewingOreRecipe (input,inputs,output));
	}
}
