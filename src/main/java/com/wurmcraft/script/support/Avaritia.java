package com.wurmcraft.script.support;


import com.google.common.base.Preconditions;
import com.wurmcraft.api.ScriptFunction;
import com.wurmcraft.common.ConfigHandler;
import com.wurmcraft.common.reference.Global;
import com.wurmcraft.script.exception.InvalidStackException;
import com.wurmcraft.script.utils.StackHelper;
import com.wurmcraft.script.utils.SupportHelper;
import com.wurmcraft.script.utils.recipe.RecipeUtils;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Avaritia extends SupportHelper {

	private List <ExtremeShapedRecipe> shaped = Collections.synchronizedList (new ArrayList <> ());
	private List <ExtremeShapelessRecipe> shapeless = Collections.synchronizedList (new ArrayList <> ());
	private List <ICompressorRecipe> compressor = Collections.synchronizedList (new ArrayList <> ());

	public Avaritia () {
		super ("avaritia");
	}

	@Override
	public void init () {
		shaped.clear ();
		shapeless.clear ();
		compressor.clear ();
		if (ConfigHandler.Script.removeAllMachineRecipes) {
			AvaritiaRecipeManager.EXTREME_RECIPES.clear ();
			AvaritiaRecipeManager.COMPRESSOR_RECIPES.clear ();
		}
	}

	@ScriptFunction
	public void addShapedExtreme (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length >= 3,"addShapedExtreme('<output> <style> <format')");
		isValid (helper,input[0]);
		ItemStack output = convertStack (helper,input[0]);
		List <Object> finalRecipe = RecipeUtils.getShapedRecipe (helper,input);
		Preconditions.checkNotNull (finalRecipe);
		ExtremeShapedRecipe recipe = new ExtremeShapedRecipe (output,CraftingHelper.parseShaped (finalRecipe.toArray (new Object[0])));
		shaped.add (recipe);
	}

	@ScriptFunction
	public void addShapelessExtreme (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length >= 2,"addShapelessExtreme('<output> <input>...')");
		isValid (helper,input[0]);
		NonNullList <Ingredient> recipeInput = NonNullList.create ();
		boolean validRecipe = true;
		for (String item : Arrays.copyOfRange (input,1,input.length))
			if (helper.convert (item) != null || helper.isOreEntry (item))
				recipeInput.add (convertIngredient (helper,item));
			else {
				validRecipe = false;
				throw new InvalidStackException ("Invalid Item '" + item + "'");
			}
		if (validRecipe) {
			ItemStack output = convertStack (helper,input[0]);
			Preconditions.checkArgument (!recipeInput.isEmpty (),"Invalid Inputs!");
			shapeless.add (new ExtremeShapelessRecipe (recipeInput,output));
		}
	}

	@ScriptFunction
	public void addCompression (StackHelper helper,String line) {
		String[] input = validate (line,line.split (" ").length == 2,"addCompression('<output> <input>')");
		isValid (helper,input[0],input[1]);
		ItemStack output = convertStack (helper,input[0]);
		compressor.add (new CompressorRecipe (output,convertStack (helper,input[1]).getCount (),false,NonNullList.from (convertIngredient (helper,input[1]))));
	}

	@Override
	public void finishSupport () {
		for (ExtremeShapedRecipe recipe : shaped)
			AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,recipe.getRecipeOutput ().toString () + (recipe.getRecipeOutput ().hasTagCompound () ? recipe.getRecipeOutput ().getTagCompound () : "")),recipe);
		for (ExtremeShapelessRecipe recipe : shapeless)
			AvaritiaRecipeManager.EXTREME_RECIPES.put (new ResourceLocation (Global.MODID,recipe.getRecipeOutput ().toString () + (recipe.getRecipeOutput ().hasTagCompound () ? recipe.getRecipeOutput ().getTagCompound () : "")),recipe);
		for (ICompressorRecipe recipe : compressor)
			AvaritiaRecipeManager.COMPRESSOR_RECIPES.put (new ResourceLocation (Global.MODID,recipe.getResult ().toString () + (recipe.getResult ().hasTagCompound () ? recipe.getResult ().getTagCompound () : "")),recipe);
	}
}
