package com.wurmcraft.wurmtweaks.script.support;

import com.google.common.base.Preconditions;
import com.wurmcraft.wurmtweaks.api.EnumInputType;
import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import com.wurmcraft.wurmtweaks.script.RecipeUtils;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

public class GalacticCraft extends ModSupport {

	@Override
	public String getModID () {
		return "galacticraftcore";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			CompressorRecipes.getRecipeList ().clear ();
			CircuitFabricatorRecipes.getRecipes ().clear ();
		}
	}

	@ScriptFunction
	public void addShapedCompressor (String line) {
		String[] input = verify (line,line.split (" ").length > 4,"addShapedCompressor(<output> <style> <format>')");
		isValid (input[0]);
		List <Object> recipe = RecipeUtils.getShapedRecipe (input);
		Preconditions.checkNotNull (recipe);
		CompressorRecipes.addRecipe (convertS (input[0]),recipe.toArray (new Object[0]));
	}

	@ScriptFunction
	public void addShapelessCompressor (String line) {
		String[] input = verify (line,(StringUtils.countMatches (line," ") >= 2 && StringUtils.countMatches (line," ") <= 10),"addShapelessCompressor('<output> <input>...')");
		isValid (input[0]);
		List <Ingredient> inputs = RecipeUtils.getShapelessRecipeItems (input,null,1);
		Preconditions.checkArgument (!inputs.isEmpty (),"Invalid Inputs!");
		CompressorRecipes.addShapelessRecipe (convertS (input[0]),inputs.toArray (new Ingredient[0]));
	}

	@ScriptFunction
	public void addCircuitFabricator (String line) {
		String[] input = verify (line,line.length () == 5,"addCircuitFabricator('<output> <input> <input2> <input3> <input4>')");
		isValid (input[0],input[1],input[2],input[3],input[4]);
		List <Object> inputs = RecipeUtils.getRecipeItems (input,1);
		Preconditions.checkArgument (!inputs.isEmpty (),"Invalid Inputs!");
		CircuitFabricatorRecipes.addRecipe (convertS (input[0]),inputs);
	}

	@ScriptFunction
	public void addSpaceStationRecipe (String line) {
		String[] input = verify (line,line.length () >= 2,"addSpaceStationRecipe('<stationID> <items>...')");
		isValid (EnumInputType.INTEGER,input[0]);
		List <Object> inputs = RecipeUtils.getRecipeItems (input,1);
		Preconditions.checkArgument (!inputs.isEmpty (),"Invalid Inputs!");
		HashMap <Object, Integer> stationRecipe = new HashMap <> ();
		for (Object item : inputs)
			if (item instanceof ItemStack)
				stationRecipe.put (item,((ItemStack) item).getCount ());
			else
				stationRecipe.put (item,16);
		GalacticraftRegistry.replaceSpaceStationRecipe (convertNI (input[0]),stationRecipe);
	}
}
