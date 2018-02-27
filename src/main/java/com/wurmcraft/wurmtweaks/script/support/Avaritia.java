package com.wurmcraft.wurmtweaks.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.reference.Global;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
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
		isValid (input[0]);
		ItemStack output = convertS (input[0]);
		List <Object> finalRecipe = RecipeUtils.getShapedRecipe (input);
		Preconditions.checkNotNull (finalRecipe);
		ExtremeShapedRecipe recipe = new ExtremeShapedRecipe (output,CraftingHelper.parseShaped (finalRecipe.toArray (new Object[0])));
		AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + recipe.hashCode ()),recipe);
	}

	@ScriptFunction
	public void addShapelessExtreme (String line) {
		String[] input = verify (line,line.split (" ").length >= 2,"addShapelessExtreme('<output> <input>...')");
		isValid (input[0]);
		NonNullList <Ingredient> recipeInput = NonNullList.create ();
		recipeInput.addAll (RecipeUtils.getShapelessRecipeItems (input,null,1));
		ItemStack output = StackHelper.convert (input[0]);
		Preconditions.checkArgument (!recipeInput.isEmpty (),"Invalid Inputs!");
		AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + recipeInput.hashCode ()),new ExtremeShapelessRecipe (recipeInput,output));
	}

	@ScriptFunction
	public void addCompression (String line) {
		String[] input = verify (line,line.split (" ").length == 2,"addCompression('<output> <input>')");
		isValid (input[0],input[1]);
		ItemStack output = convertS (input[0]);
		AvaritiaRecipeManager.COMPRESSOR_RECIPES.put (new ResourceLocation (Global.MODID,output.getUnlocalizedName ().substring (5) + convertS (input[1]).hashCode ()),new CompressorRecipe (output,convertS (input[1]).getCount (),false,NonNullList.from (convertI (input[1]))));
	}
}
