package com.wurmcraft.wurmtweaks.script.support;

import com.wurmcraft.wurmtweaks.api.ScriptFunction;
import com.wurmcraft.wurmtweaks.common.ConfigHandler;
import com.wurmcraft.wurmtweaks.script.EnumInputType;
import com.wurmcraft.wurmtweaks.script.ModSupport;
import nc.recipe.NCRecipes;

public class NuclearCraft extends ModSupport {

	@Override
	public String getModID () {
		return "nuclearcraft";
	}

	@Override
	public void init () {
		if (ConfigHandler.removeAllMachineRecipes) {
			NCRecipes.Type.PRESSURIZER.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.ISOTOPE_SEPARATOR.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.MANUFACTORY.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.ALLOY_FURNACE.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.CHEMICAL_REACTOR.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.SUPERCOOLER.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.INFUSER.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.INGOT_FORMER.getRecipeHandler ().recipes.clear ();
			NCRecipes.Type.MELTER.getRecipeHandler ().recipes.clear ();
		}
	}

	@ScriptFunction
	public void addManufactory (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addManufactory('<output> <input> <time>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		NCRecipes.Type.MANUFACTORY.getRecipeHandler ().addRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}

	@ScriptFunction
	public void addIsotopeSeparator (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addIsotopeSeparator('<output> <output2> <input> <time')");
		isValid (input[0],input[1],input[2]);
		isValid (EnumInputType.INTEGER,input[3]);
		NCRecipes.Type.ISOTOPE_SEPARATOR.getRecipeHandler ().addRecipe (convertS (input[2]),convertS (input[0]),convertS (input[1]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addAlloyFurnace (String line) {
		String[] input = verify (line,line.split (" ").length == 4,"addAlloyFurnace('<output> <input> <input2> <time>')");
		isValid (input[0],input[1],input[2]);
		isValid (input[3]);
		NCRecipes.Type.ISOTOPE_SEPARATOR.getRecipeHandler ().addRecipe (convertS (input[1]),convertS (input[2]),convertS (input[0]),convertNI (input[3]));
	}

	@ScriptFunction
	public void addSupercooler (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addSupercooler('<*output> <*input> <time>')");
		isValid (EnumInputType.FLUID,input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		NCRecipes.Type.SUPERCOOLER.getRecipeHandler ().addRecipe (convertF (input[1]),convertF (input[0]),convertNI (input[2]));
	}

	@ScriptFunction
	public void addPressurizer (String line) {
		String[] input = verify (line,line.split (" ").length == 3,"addPressurizer('<output> <input> <time>')");
		isValid (input[0],input[1]);
		isValid (EnumInputType.INTEGER,input[2]);
		NCRecipes.Type.PRESSURIZER.getRecipeHandler ().addRecipe (convertS (input[1]),convertS (input[0]),convertNI (input[2]));
	}
}
